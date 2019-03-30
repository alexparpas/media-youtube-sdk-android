package com.alexparpas.media.youtube.core.model

import com.google.gson.annotations.SerializedName

data class ApiVideoResponse(
        val id: String,
        val items: List<ApiVideoItem>
)

data class ApiVideoItem(
        val id: String,
        val snippet: ApiSnippet,
        val statistics: ApiStatistics,
        val contentDetails: ApiContentDetails
)

data class ApiSnippet(
        val publishedAt: String,
        val channelId: String,
        val title: String,
        val description: String,
        val thumbnails: ApiThumbnails,
        val channelTitle: String,
        val tags: List<String>,
        val categoryId: String,
        val liveBroadcastContent: String,
        val localized: ApiLocalized
)

data class ApiLocalized(
        val title: String,
        val description: String
)

data class ApiThumbnails(
        val default: ApiDefault,
        val medium: ApiDefault,
        val high: ApiDefault,
        val standard: ApiDefault,
        @SerializedName("maxres") val maxRes: ApiDefault
)

data class ApiDefault(
        val url: String,
        val width: Long,
        val height: Long
)

data class ApiStatistics(
        val viewCount: String,
        val likeCount: String,
        val dislikeCount: String,
        val favoriteCount: String,
        val commentCount: String
)

data class ApiContentDetails(
        val duration: String,
        val dimension: String,
        val definition: String,
        val caption: String,
        val licensedContent: Boolean,
        val contentRating: ApiContentRating,
        val projection: String
)

data class ApiContentRating(
        val ytRating: String
)