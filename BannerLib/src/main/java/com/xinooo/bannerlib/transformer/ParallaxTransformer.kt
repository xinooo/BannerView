package com.xinooo.bannerlib.transformer

import android.view.View
import androidx.viewpager2.widget.ViewPager2

class ParallaxTransformer : ViewPager2.PageTransformer {

    override fun transformPage(page: View, position: Float) {
        page.scrollX = when {
            position < -1 -> (page.width * 0.75 * -1).toInt()
            position <= 1 -> (page.width * 0.75 * position).toInt()
            else -> (page.width * 0.75).toInt()
        }
    }
}