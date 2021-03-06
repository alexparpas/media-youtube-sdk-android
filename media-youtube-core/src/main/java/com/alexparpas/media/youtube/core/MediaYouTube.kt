package com.alexparpas.media.youtube.core

import android.app.Application
import com.alexparpas.media.youtube.core.api.YouTubeMediaService
import com.alexparpas.media.youtube.core.data.LocalStorage
import com.alexparpas.media.youtube.core.data.YouTubeMediaRepository
import com.alexparpas.media.youtube.core.data.YouTubeVideoMapper
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object MediaYouTube {
    private lateinit var app: Application
    private lateinit var apiKey: String

    val youTubeMediaRepository by lazy {
        YouTubeMediaRepository(
                apiKey,
                LocalStorage,
                Injection.getYouTubeService(),
                YouTubeVideoMapper()
        )
    }

    fun init(app: Application, apiKey: String) {
        MediaYouTube.app = app
        MediaYouTube.apiKey = apiKey
    }

    internal object Injection {
        fun getYouTubeService(): YouTubeMediaService =
                Retrofit.Builder()
                        .baseUrl(app.getString(R.string.youtube_api_base_url))
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .build()
                        .create(YouTubeMediaService::class.java)
    }
}