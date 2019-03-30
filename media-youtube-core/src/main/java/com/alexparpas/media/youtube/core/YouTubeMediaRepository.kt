package com.alexparpas.media.youtube.core

import io.reactivex.Single

class YouTubeMediaRepository(
        private val apiKey: String,
        private val localStorage: LocalStorage,
        private val service: YouTubeMediaService,
        private val videoMapper: YouTubeVideoMapper
) {

    fun getVideoIds(sections: List<VideoSection>): Single<List<String>> =
            Single.just(sections)
                    .map {
                        it.map { section -> section.videoIds }.flatten()
                    }

    fun getVideos(sections: List<VideoSection>, videoIds: List<String>): Single<List<MediaItem>> {
        return getCachedVideos(videoIds).doOnError {
            if (it is NotCachedException) {
                getVideosRemote(videoIds)
            } else {
                Single.error(it)
            }
        }.map {
            videoMapper.map(sections, it)
        }
    }

    private fun getCachedVideos(videoIds: List<String>): Single<List<VideoBinding>> =
            Single.just(localStorage.getVideos(videoIds))

    private fun getVideosRemote(videoIds: List<String>): Single<List<VideoBinding>> {
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
