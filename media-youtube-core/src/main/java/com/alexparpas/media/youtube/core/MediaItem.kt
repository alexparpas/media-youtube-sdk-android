package com.alexparpas.media.youtube.core

sealed class MediaItem
class CategoryItem(val categoryName: String, val videoIds: List<String>): MediaItem()
class VideosItem(val videos: List<VideoBinding>): MediaItem()