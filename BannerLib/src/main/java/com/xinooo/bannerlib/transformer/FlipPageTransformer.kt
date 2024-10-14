package com.xinooo.bannerlib.transformer

import android.view.View
import androidx.viewpager2.widget.ViewPager2

class FlipPageTransformer : ViewPager2.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        if (position <= 0.0f) {
            val rotation = ROTATION * position
            page.rotationY = rotation
        } else if (position <= 1.0f) {
            val rotation = ROTATION * position
            page.rotationY = rotation
        }

        page.pivotX = page.width * 0.5f
        page.pivotY = page.height * 0.5f
        page.translationX = -page.width * position

        if (position > -0.5f && position < 0.5f) {
            page.visibility = View.VISIBLE
        } else {
            page.visibility = View.INVISIBLE
        }
    }

    companion object {
        private const val ROTATION = 180.0f
    }
}