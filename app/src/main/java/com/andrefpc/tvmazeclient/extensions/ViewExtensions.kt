package com.andrefpc.tvmazeclient.extensions

import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.Transformation
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity

object ViewExtensions {
    /**
     * Fade In animation
     */
    fun View.fadeIn(duration: Long = 300L) {
        this@fadeIn.visibility = View.VISIBLE
        val anim = AlphaAnimation(0f, 1f)
        anim.duration = duration
        anim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                this@fadeIn.clearAnimation()
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        this@fadeIn.startAnimation(anim)
    }

    /**
     * Fade Out animation
     */
    fun View.fadeOut(duration: Long = 300L, visibilityToGone: Boolean = true) {
        val anim = AlphaAnimation(1f, 0f)
        anim.duration = duration
        anim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                if (visibilityToGone) this@fadeOut.visibility = View.GONE
                else this@fadeOut.visibility = View.INVISIBLE
                this@fadeOut.clearAnimation()
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        this@fadeOut.startAnimation(anim)
    }

    /**
     * Hide the keyboard
     */
    fun View.hideKeyboard() {
        val imm =
            context.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(this.windowToken, 0)
    }
}
