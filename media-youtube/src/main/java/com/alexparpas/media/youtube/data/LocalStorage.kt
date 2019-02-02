package com.alexparpas.media.youtube.data

object LocalStorage {
    private lateinit var videos: List<VideoMinimal> //videoId | video

    fun getVideos(videoIds: List<String>): List<VideoMinimal> {
        if (::videos.isInitialized) {
            return videos.filter { videoIds.contains(it.id) }.distinctBy { it.id }
        } else {
            throw NotCachedException()
        }
    }

    fun cacheVideos(videos: List<VideoMinimal>) {
        this.videos = videos
    }
}