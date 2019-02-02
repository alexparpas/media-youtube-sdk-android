package com.alexparpas.media.youtube.data

import io.reactivex.Single

class YouTubeMediaRepository(
        private val apiKey: String,
        private val localStorage: LocalStorage,
        private val service: YouTubeMediaService
) {

    fun getVideoIds(sections: List<VideoSection>): Single<List<String>> =
            Single.just(sections)
                    .map {
                        it.map { section -> section.videoIds }.flatten()
                    }

    fun getVideos(videoIds: List<String>): Single<List<VideoMinimal>> {
        return try {
            getCachedVideos(videoIds)
        } catch (e: Throwable) {
            if (e is NotCachedException) {
                getVideosRemote(videoIds)
            } else {
                Single.error(e)
            }
        }
    }

    private fun getCachedVideos(videoIds: List<String>) = Single.just(localStorage.getVideos(videoIds))

    private fun getVideosRemote(videoIds: List<String>): Single<List<VideoMinimal>> {
        return service.getVideos(
                apiKey = apiKey,
                part = "snippet,contentDetails,statistics",
                ids = videoIds.joinToString(",")
        ).map { videoItems ->
            videoItems.items.map { it.toMinimal() }
        }.doOnSuccess {
            localStorage.cacheVideos(it)
        }

    }
}
