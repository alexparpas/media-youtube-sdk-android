package com.alexparpas.media.youtube.core.data

import com.alexparpas.media.youtube.core.model.*
import java.text.DecimalFormat

class YouTubeVideoMapper internal constructor() {

    fun map(videoResponse: ApiVideoResponse): List<VideoItem> {
        return videoResponse.items.map { item ->
            with(item) {
                val viewCountFormatted = map(statistics.viewCount.toDoubleOrNull())
                VideoItem(
                        id = id,
                        title = snippet.title,
                        channelTitle = snippet.channelTitle,
                        thumbnailUrl = snippet.thumbnails.medium.url,
                        likeCount = statistics.likeCount,
                        viewCount = viewCountFormatted,
                        dislikeCount = statistics.dislikeCount,
                        commentCount = statistics.commentCount,
                        duration = contentDetails.duration
                )
            }
        }
    }

    fun map(sections: List<VideoSection>, videos: List<VideoItem>): List<MediaItem> =
            sections.map { map(it, videos) }.flatten()

    private fun map(it: VideoSection, videos: List<VideoItem>): MutableList<MediaItem> {
        val items = mutableListOf<MediaItem>()

        with(it) {
            items.add(CategoryItem(title, description, videoIds))

            videoIds.map { id ->
                videos.first { video ->
                    video.id == id
                }
            }.let {
                items.add(VideosItem(it))
            }
        }
        return items
    }


    fun map(viewCount: Double?): String {
        return if (viewCount != null) {
            if (viewCount < 1000) return "${viewCount.toInt()} views"
            val exp = (Math.log(viewCount) / Math.log(1000.0)).toInt()
            val format = DecimalFormat("0.#")
            val value = format.format(viewCount / Math.pow(1000.0, exp.toDouble()))
            return String.format("%s%c", value, "kMBTPE"[exp - 1]) + " views"
        } else {
            ""
        }
    }
}