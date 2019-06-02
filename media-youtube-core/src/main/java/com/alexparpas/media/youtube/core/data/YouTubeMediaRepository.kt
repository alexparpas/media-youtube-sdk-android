package com.alexparpas.media.youtube.core.data

import com.alexparpas.media.youtube.core.api.YouTubeMediaService
import com.alexparpas.media.youtube.core.model.MediaItem
import com.alexparpas.media.youtube.core.model.VideoItem
import com.alexparpas.media.youtube.core.model.VideoSection
import com.alexparpas.media.youtube.core.util.NotCachedException
import io.reactivex.Single

class YouTubeMediaRepository internal constructor(
        private val apiKey: String,
        private val localStorage: LocalStorage,
        private val service: YouTubeMediaService,
        private val videoMapper: YouTubeVideoMapper
) {

    fun sortByOrder(sections: List<VideoSection>): Single<List<VideoSection>> =
            Single.just(sections).map {
                sections.sortedBy { it.order }
            }

    fun getVideoIds(sections: List<VideoSection>): Single<List<String>> =
            Single.just(sections)
                    .map {
                        it.map { section ->
                            section.videoIds ?: listOf()
                        }.flatten()
                    }

    fun getVideos(sections: List<VideoSection>, videoIds: List<String>): Single<List<MediaItem>> {
        return getVideos(videoIds).map {
            videoMapper.map(sections, it)
        }
    }

    fun getVideos(videoIds: List<String>): Single<List<VideoItem>> {
        return getCachedVideos(videoIds).onErrorResumeNext {
            if (it is NotCachedException) {
                getVideosRemote(videoIds)
            } else {
                Single.error(it)
            }
        }
    }

    private fun getCachedVideos(videoIds: List<String>): Single<List<VideoItem>> =
            localStorage.getVideos(videoIds)

    private fun getVideosRemote(videoIds: List<String>): Single<List<VideoItem>> {
        return service.getVideos(
                apiKey = apiKey,
                part = "snippet,contentDetails,statistics",
                ids = videoIds.joinToString(",")
        ).map { videoItems ->
            videoMapper.map(videoItems)
        }.doOnSuccess {
            LocalStorage.cacheVideos(it)
        }
    }
}
