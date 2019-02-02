package com.alexparpas.media.youtube.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class VideoSection(val categoryName: String, val videoIds: List<String>) : Parcelable

fun VideoSection.toMediaBindingItem(videos: List<VideoMinimal>): List<MediaItem> {
    val items = mutableListOf<MediaItem>()

    items.add(CategoryItem(categoryName, videoIds))

    videoIds.map { id ->
        videos.first { video ->
            video.id == id
        }
    }.let {
        items.add(VideosItem(it))
    }

    return items
}