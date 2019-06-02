package com.alexparpas.media.youtube.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alexparpas.media.youtube.core.MediaYouTube
import com.alexparpas.media.youtube.core.model.VideoSection
import com.alexparpas.media.youtube.sample.util.JsonUtils
import com.alexparpas.media.youtube.ui.MediaYouTubeUi
import com.google.gson.reflect.TypeToken

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MediaYouTube.init(application, getString(R.string.youtube_api_key))

        supportFragmentManager.beginTransaction().replace(
                R.id.frag_ct,
                MediaYouTubeUi.getMainMediaFragment(getSections()!!),
                "frag"
        ).commit()
    }

    private fun getSections() =
            JsonUtils.fromJson<ArrayList<VideoSection>>(
                    fileName = "media.json",
                    type = object : TypeToken<ArrayList<VideoSection>>() {}.type
            )
}
