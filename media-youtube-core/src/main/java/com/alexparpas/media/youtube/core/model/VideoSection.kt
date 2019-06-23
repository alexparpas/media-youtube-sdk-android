package com.alexparpas.media.youtube.core.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class VideoSection(
        val title: String,
        val description: String? = null,
        val videoIds: List<String>?,
        val order: Int = Integer.MAX_VALUE,
        val isNew: Boolean = false
) : Parcelable