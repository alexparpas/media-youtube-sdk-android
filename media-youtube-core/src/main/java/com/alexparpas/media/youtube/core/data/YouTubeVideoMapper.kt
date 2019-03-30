package com.alexparpas.media.youtube.core.data

import com.alexparpas.media.youtube.core.model.*
import java.text.SimpleDateFormat
import java.util.*

class YouTubeVideoMapper internal constructor() {

    fun map(videoResponse: ApiVideoResponse): List<VideoItem> {
        return videoResponse.items.map { item ->
            with(item) {
                SimpleDateFormat("HH:mm:ssZ", Locale.UK).format(Date())
                VideoItem(
                        id = id,
                        title = snippet.title,
                        channelTitle = snippet.channelTitle,
                        thumbnailUrl = snippet.thumbnails.medium.url,
                        likeCount = statistics.likeCount,
                        viewCount = "${statistics.viewCount} views",
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
}