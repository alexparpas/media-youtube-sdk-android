package com.alexparpas.media.youtube.data

sealed class MediaItem
class CategoryItem(val categoryName: String, val videoIds: List<String>): MediaItem()
class VideosItem(val videos: List<VideoMinimal>): MediaItem()