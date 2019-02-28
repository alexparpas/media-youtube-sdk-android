package com.alexparpas.media.youtube.core

sealed class MediaItem
class CategoryItem(
        val title: String,
        val description: String,
        val videoIds: List<String>
) : MediaItem()

class VideosItem(val videos: List<VideoBinding>) : MediaItem()