package com.alexparpas.media.youtube.core.util

import android.content.res.Resources
import android.view.View

internal val Int.dp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()

internal val Int.px: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()


//This function works only if the Views are in the same parent
internal fun onlyOneViewVisible(views: List<View>, visibleView: View) {
    for (view in views) {
        view.visibility = if (view == visibleView) View.VISIBLE else View.GONE
    }
}