package com.xinooo.bannerlib.transformer

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.xinooo.bannerlib.ext.dp
import kotlin.math.abs

class CardTransformer(
    private var numberOfStacked: Int = 2,   //顯示頁數(須同步設置pageLimit)
    private var mScaleOffset: Int = 10.dp   //偏移量
): ViewPager2.PageTransformer {

    /*
     * ViewPager2 與 ViewPager相反，越後面的頁面在越上層
     * 需設定 Z 軸順序，越接近 0 越在上層
     */

    override fun transformPage(page: View, position: Float) {
        when {
            //前一頁
            position < -1 -> page.alpha = 0f
            //當前頁
            position <= 0.0f -> {
                page.alpha = 1f
                page.translationX = page.width * position
                page.translationY = 0f
                page.translationZ = 1f - abs(position) // 設定 Z 軸順序，越接近 0 越在上層
            }
            //下一頁 ~ 顯示頁數+1
            position < numberOfStacked + 1 -> {
                if (position > numberOfStacked && position <= numberOfStacked + 1) {
                    page.alpha = 1f - (position - numberOfStacked)
                }else {
                    page.alpha = 1f
                }
                page.translationX = (-page.width + mScaleOffset) * position
                page.translationY = mScaleOffset * position
                page.translationZ = -1f * position
            }
            else -> page.alpha = 0f
        }
    }
}