package com.alexparpas.media.youtube.core

object LocalStorage {
    private lateinit var videos: List<VideoBinding> //videoId | video

    fun getVideos(videoIds: List<String>): List<VideoBinding> {
        if (LocalStorage::videos.isInitialized) {
            return videos.filter { videoIds.contains(it.id) }.distinctBy { it.id }
        } else {
            throw NotCachedException()
        }
    }

    fun cacheVideos(videos: List<VideoBinding>) {
        this.videos = videos
    }
}