package com.alexparpas.media.youtube.core.data

sealed class MediaItem

class CategoryItem(
        val title: String,
        val description: String,
        val videoIds: List<String>
) : MediaItem()

class VideosItem(val videos: List<VideoItem>) : MediaItem()

data class VideoItem(
        val id: String,
        val title: String,
        val channelTitle: String,
        val thumbnailUrl: String,
        val likeCount: String,
        val viewCount: String,
        val dislikeCount: String,
        val commentCount: String,
        val duration: String
)