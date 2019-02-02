package com.alexparpas.media.youtube.data

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface YouTubeMediaService {
    @GET("v3/videos")
    fun getVideos(
            @Query("key") apiKey: String,
            @Query("part") part: String,
            @Query("id") ids: String
    ): Single<VideoResponse>
}