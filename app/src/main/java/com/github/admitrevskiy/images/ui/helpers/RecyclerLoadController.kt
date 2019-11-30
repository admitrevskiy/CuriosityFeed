package com.github.admitrevskiy.images.ui.helpers

import android.util.Log

/**
 * This controller is necessary to avoid extra photos pack load.
 */
internal class RecyclerLoadController(private val sizeListener: () -> Unit,
                                      private val getItemCount: () -> Int,
                                      private val loadFactor: Int = 3) {

    private val tag = "RecyclerLoadController"

    fun onLoad(position: Int) {
        Log.d(tag, "Image loading on position $position succeed")
        notifyListener(position)
    }

    fun onLoadError(position: Int, t: Throwable?) {
        Log.w(tag, "Failed to load image on position $position", t)
        notifyListener(position)
    }

    @Synchronized
    private fun notifyListener(position: Int) {
        if (position == getItemCount() - loadFactor) {
            Log.d(tag, "Notify SizeListener: Current position $position, Items count: ${getItemCount()}. We need more photos!")
            sizeListener()
        }
    }
}