package com.alexparpas.media.youtube.core.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class VideoSection(
        val title: String,
        val description: String,
        val videoIds: List<String>
): Parcelable