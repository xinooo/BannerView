package com.xinooo.bannerlib.transformer

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs

class FadePageTransformer: ViewPager2.PageTransformer {

    /*
     * ViewPager2 與 ViewPager相反，越後面的頁面在越上層
     * 需設定 Z 軸順序，越接近 0 越在上層
     */

    override fun transformPage(page: View, position: Float) {
        //頁面置中
        page.translationX = -position * page.width

        when {
            //前一頁
            position < -1 -> {
                page.alpha = 0f
                page.translationZ = -1f
            }
            //當前頁&下一頁
            position <= 1 -> {
                page.alpha = 1 - abs(position)
                val scaleFactor = 0.85f + (1 - abs(position)) * 0.15f
                page.scaleY = scaleFactor
                page.scaleX = scaleFactor
                page.translationZ = 1f - abs(position)
            }
            else -> {
                page.alpha = 0f
                page.translationZ = -1f
            }
        }
    }

}