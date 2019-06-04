package com.alexparpas.media.youtube.core.data

import com.alexparpas.media.youtube.core.model.VideoItem
import com.alexparpas.media.youtube.core.util.NotCachedException
import io.reactivex.Single

object LocalStorage {
    private var videos: List<VideoItem> = listOf()

    fun getVideos(videoIds: List<String>): Single<List<VideoItem>> {
        val videos = videos.filter { videoIds.contains(it.id) }
                .takeIf { it.isNotEmpty() }
                ?.distinctBy { it.id }
                ?: listOf()

        return if (videos.isNotEmpty()) {
            Single.just(videos)
        } else {
            Single.error(NotCachedException())
        }
    }

    fun cacheVideos(videos: List<VideoItem>) {
        LocalStorage.videos = videos
    }
}