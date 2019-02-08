package com.alexparpas.media.youtube.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alexparpas.media.youtube.core.MediaYouTube
import com.alexparpas.media.youtube.core.VideoSection
import com.alexparpas.media.youtube.ui.MediaYouTubeUi

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MediaYouTube.init(application, getString(R.string.youtube_api_key))

        supportFragmentManager.beginTransaction().replace(
                R.id.frag_ct,
                MediaYouTubeUi.getMainMediaFragment(getSections()),
                "frag"
        ).commit()
    }

    private fun getSections() = arrayListOf(
            VideoSection(categoryName = "MK11", videoIds = listOf("iZqZvHf_TsA", "CShq7wrgM5M", "BLUpVgaBNn8")),
            VideoSection(categoryName = "Learn", videoIds = listOf("NPBHbj317ro", "Vm7eg_KkAnA", "5-640npgS-w")),
            VideoSection(categoryName = "JINAMOUNAINAI", videoIds = listOf("uBtgRI6kC6w", "2R0l4m5t0Xk", "mgJu5QxKaKo", "uf6HEhnw93U", "EfDQ5fVeY9c", "vv0_jD9BZJA")),
            VideoSection(categoryName = "Man0_GRC", videoIds = listOf("0Tm8csb4O00", "9_PJWfrBiYU", "RqArBLi-aac")),
            VideoSection(categoryName = "BornThroughAshes", videoIds = listOf("tJDyg5rnYBs", "2S8hz0t7g6A", "HwnKvSyjECM", "INRtm2xLo9Q"))
    )
}
