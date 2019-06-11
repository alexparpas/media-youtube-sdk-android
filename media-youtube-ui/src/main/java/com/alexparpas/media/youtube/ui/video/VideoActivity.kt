package com.alexparpas.media.youtube.ui.video

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alexparpas.media.youtube.R
import com.alexparpas.media.youtube.ui.MediaYouTubeUi
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.loadOrCueVideo
import kotlinx.android.synthetic.main.myt_activity_video.*


internal class VideoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.myt_activity_video)

        val videoId = intent.getStringExtra(MediaYouTubeUi.ARG_VIDEO_ID)
        initPlayer(videoId)
    }

    override fun onBackPressed() {
        if (youtube_player_view.isFullScreen()) {
            exitFullScreen()
        } else {
            super.onBackPressed()
        }
    }

    private fun initPlayer(videoId: String) {
        // The player will automatically release itself when the activity is destroyed.
        // The player will automatically pause when the activity is stopped
        lifecycle.addObserver(youtube_player_view)

        youtube_player_view.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                super.onReady(youTubePlayer)

                youTubePlayer.loadOrCueVideo(
                        lifecycle = lifecycle,
                        videoId = videoId,
                        startSeconds = 0f
                )
            }
        })
        addFullScreenListenerToPlayer()
    }

    private fun addFullScreenListenerToPlayer() {
        youtube_player_view.addFullScreenListener(object : YouTubePlayerFullScreenListener {
            override fun onYouTubePlayerEnterFullScreen() {
                enterFullScreen()
            }

            override fun onYouTubePlayerExitFullScreen() {
                exitFullScreen()
            }
        })
    }

    private fun enterFullScreen() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        youtube_player_view.enterFullScreen()
    }

    private fun exitFullScreen() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        youtube_player_view.exitFullScreen()
    }
}