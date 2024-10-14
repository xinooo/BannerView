package com.xinooo.bannerlib.transformer

import android.view.View
import androidx.viewpager2.widget.ViewPager2

class StackTransformer : ViewPager2.PageTransformer {

    override fun transformPage(page: View, position: Float) {
        page.translationX = if (position > 0) 0f else -page.width * position
        page.alpha = if (position <= -1f || position >= 1f) 0f else 1f
    }
}