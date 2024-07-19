package com.andrefpc.tvmazeclient.util.extensions

import android.graphics.drawable.Drawable
import androidx.appcompat.widget.AppCompatImageView
import com.andrefpc.tvmazeclient.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

object ImageViewExtensions {
    /**
     * Load an image into the AppCompatImageView from a remote video URL
     * @param url The url of the remote image
     * @param returnError Callback with the image receive error when try to loading
     */
    fun AppCompatImageView.loadImage(
        url: String?,
        returnError: () -> Unit = {},
        returnSuccess: () -> Unit = {}
    ) {
        if (!url.isNullOrEmpty()) {
            Glide.with(this).load(url).placeholder(R.drawable.ic_no_image)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        returnError()
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        returnSuccess()
                        return false
                    }
                }).into(this)
        } else {
            this.setImageResource(R.drawable.ic_no_image)
            returnError()
        }
    }
}
