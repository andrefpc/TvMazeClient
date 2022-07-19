package com.andrefpc.tvmazeclient.widget

import android.content.Context
import android.util.AttributeSet
import android.view.animation.AnimationUtils
import android.widget.ViewFlipper
import com.andrefpc.tvmazeclient.R


class ShimmerLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : ViewFlipper(context, attrs) {
    var shimmer: Shimmer? = null

    init {
        shimmer = Shimmer(context)
        addView(shimmer)
        inAnimation = AnimationUtils.loadAnimation(context, R.anim.fade_in)
        outAnimation = AnimationUtils.loadAnimation(context, R.anim.fade_out)
    }

    fun startProgress() {
        displayedChild = 0
        shimmer?.startProgress()
    }

    fun stopProgress() {
        shimmer?.stopProgress()
        displayedChild = 1
    }
}