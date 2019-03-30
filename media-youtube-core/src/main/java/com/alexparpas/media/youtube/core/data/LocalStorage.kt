package com.alexparpas.media.youtube.core.data

import com.alexparpas.media.youtube.core.util.NotCachedException
import io.reactivex.Single

object LocalStorage {
    private lateinit var videos: List<VideoItem>

    fun getVideos(videoIds: List<String>): Single<List<VideoItem>> {
        return if (LocalStorage::videos.isInitialized) {
            Single.just(videos.filter { videoIds.contains(it.id) }.distinctBy { it.id })
        } else {
            Single.error(NotCachedException())
        }
    }

    fun cacheVideos(videos: List<VideoItem>) {
        LocalStorage.videos = videos
    }
}