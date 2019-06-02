package com.alexparpas.media.youtube.core.data

import com.alexparpas.media.youtube.core.model.*
import java.text.DecimalFormat

class YouTubeVideoMapper internal constructor() {

    fun map(videoResponse: ApiVideoResponse): List<VideoItem> {
        return videoResponse.items.map { item ->
            with(item) {
                val viewCountFormatted = map(statistics.viewCount.toDoubleOrNull())
                val durationFormatted = map(contentDetails.duration)
                VideoItem(
                        id = id,
                        title = snippet.title,
                        channelTitle = snippet.channelTitle,
                        thumbnailUrl = snippet.thumbnails.medium.url,
                        likeCount = statistics.likeCount,
                        viewCount = viewCountFormatted,
                        dislikeCount = statistics.dislikeCount,
                        commentCount = statistics.commentCount,
                        duration = durationFormatted
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

    fun map(durationRaw: String): String {
        val millis = getDurationMillis(durationRaw)
        return getDurationFormatted(millis)
    }

    //Code from https://stackoverflow.com/questions/16812593/how-to-convert-youtube-api-v3-duration-in-java
    //Not the most elegant solution, but works and is well tested, so leaving it for now.
    private fun getDurationMillis(durationRaw: String): Long {
        var time = durationRaw.substring(2)
        var duration = 0L
        val indexs = arrayOf(arrayOf("H", 3600), arrayOf("M", 60), arrayOf("S", 1))
        for (i in indexs.indices) {
            val index = time.indexOf(indexs[i][0] as String)
            if (index != -1) {
                val value = time.substring(0, index)
                duration += (Integer.parseInt(value) * indexs[i][1] as Int * 1000).toLong()
                time = time.substring(value.length + 1)
            }
        }
        return duration
    }

    private fun getDurationFormatted(millis: Long): String {
        val second = millis / 1000 % 60
        val minute = millis / (1000 * 60) % 60
        val hour = millis / (1000 * 60 * 60) % 24

        return if (hour == 0L) {
            String.format("%02d:%02d", minute, second)
        } else {
            String.format("%02d:%02d:%02d", hour, minute, second)
        }
    }
}