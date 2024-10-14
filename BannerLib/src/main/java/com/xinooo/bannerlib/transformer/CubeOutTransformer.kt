package com.xinooo.bannerlib.transformer

import android.view.View
import androidx.viewpager2.widget.ViewPager2

class CubeOutTransformer: ViewPager2.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        page.pivotX = if (position < 0f) page.width.toFloat() else 0f
        page.pivotY = page.height * 0.5f
        page.rotationY = 90f * position
        page.alpha = if (position <= -1f || position >= 1f) 0f else 1f
    }
}