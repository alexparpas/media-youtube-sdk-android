package com.alexparpas.media.youtube.core

import com.google.gson.annotations.SerializedName

data class VideoResponse(
        val id: String,
        val items: List<VideoItem>
)

data class VideoItem(
        val id: String,
        val snippet: Snippet,
        val statistics: Statistics,
        val contentDetails: ContentDetails
)

data class Snippet(
        val publishedAt: String,
        val channelId: String,
        val title: String,
        val description: String,
        val thumbnails: Thumbnails,
        val channelTitle: String,
        val tags: List<String>,
        val categoryId: String,
        val liveBroadcastContent: String,
        val localized: Localized
)

data class Localized(
        val title: String,
        val description: String
)

data class Thumbnails(
        val default: Default,
        val medium: Default,
        val high: Default,
        val standard: Default,
        @SerializedName("maxres") val maxRes: Default
)

data class Default(
        val url: String,
        val width: Long,
        val height: Long
)

data class Statistics(
        val viewCount: String,
        val likeCount: String,
        val dislikeCount: String,
        val favoriteCount: String,
        val commentCount: String
)

data class ContentDetails(
        val duration: String,
        val dimension: String,
        val definition: String,
        val caption: String,
        val licensedContent: Boolean,
        val contentRating: ContentRating,
        val projection: String
)

data class ContentRating(
        val ytRating: String
)

data class VideoBinding(
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

fun VideoItem.toBinding(): VideoBinding {
    return VideoBinding(
            id = id,
            title = snippet.title,
            channelTitle = snippet.channelTitle,
            thumbnailUrl = snippet.thumbnails.medium.url,
            likeCount = statistics.likeCount,
            viewCount = statistics.viewCount,
            dislikeCount = statistics.dislikeCount,
            commentCount = statistics.commentCount,
            duration = contentDetails.duration
    )
}