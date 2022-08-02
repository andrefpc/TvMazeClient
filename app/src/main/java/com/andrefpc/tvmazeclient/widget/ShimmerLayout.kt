package com.andrefpc.tvmazeclient.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.AnimationUtils
import android.widget.ViewFlipper
import com.andrefpc.tvmazeclient.R
import com.andrefpc.tvmazeclient.databinding.LayoutEmptyBinding
import com.andrefpc.tvmazeclient.databinding.ModalEpisodeBinding


class ShimmerLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : ViewFlipper(context, attrs) {
    var shimmer: Shimmer? = null

    init {
        shimmer = Shimmer(context)
        addView(shimmer)

        val emptyBinding = LayoutEmptyBinding.inflate(
            LayoutInflater.from(context),
            null,
            false
        )
        addView(emptyBinding.root)
        inAnimation = AnimationUtils.loadAnimation(context, R.anim.fade_in)
        outAnimation = AnimationUtils.loadAnimation(context, R.anim.fade_out)
    }

    fun startProgress() {
        displayedChild = 0
        shimmer?.startProgress()
    }

    fun showEmpty() {
        shimmer?.stopProgress()
        displayedChild = 1
    }

    fun stopProgress() {
        shimmer?.stopProgress()
        displayedChild = 2
    }
}