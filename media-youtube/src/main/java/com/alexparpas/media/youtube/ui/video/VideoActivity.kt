package com.alexparpas.media.youtube.ui.video

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.alexparpas.media.youtube.MediaYouTube
import com.alexparpas.media.youtube.R
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.YouTubePlayerFullScreenListener
import kotlinx.android.synthetic.main.activity_video.*

class VideoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)

        val videoId = intent.getStringExtra(MediaYouTube.ARG_VIDEO_ID)
        lifecycle.addObserver(youtube_player_view)

        youtube_player_view.initialize({ youTubePlayer ->
            youTubePlayer.addListener(object : AbstractYouTubePlayerListener() {
                override fun onReady() {
                    super.onReady()
                    youTubePlayer.loadVideo(videoId, 0f)
                }
            })
        }, true)

        youtube_player_view.addFullScreenListener(object : YouTubePlayerFullScreenListener {
            override fun onYouTubePlayerEnterFullScreen() {
                hideSystemUI(window.decorView)
            }

            override fun onYouTubePlayerExitFullScreen() {
                //no-op
            }
        })

        youtube_player_view.enterFullScreen()
    }

    private fun hideSystemUI(decorView: View) {
        decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }
}