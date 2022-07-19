package com.andrefpc.tvmazeclient.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.andrefpc.tvmazeclient.databinding.LayoutShimmerBinding

class Shimmer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : LinearLayout(context, attrs, defStyleAttr) {
    private val binding: LayoutShimmerBinding =
        LayoutShimmerBinding.inflate(LayoutInflater.from(context), this, true)

    fun startProgress() {
        binding.shimmer.visibility = View.VISIBLE
        binding.shimmer.startShimmer()
    }

    fun stopProgress() {
        binding.shimmer.visibility = View.GONE
        binding.shimmer.stopShimmer()
    }
}