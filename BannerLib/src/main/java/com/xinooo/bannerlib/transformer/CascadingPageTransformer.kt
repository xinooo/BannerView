package com.xinooo.bannerlib.transformer

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.xinooo.bannerlib.ext.dp
import kotlin.math.abs

class CascadingPageTransformer(
    private var numberOfStacked: Int = 2,   //顯示頁數(須同步設置pageLimit)
    private var mScaleOffset: Int = 20.dp,   //偏移量
    private var nextPlace: NextPlace = NextPlace.TOP,     //後頁偏移位置
): ViewPager2.PageTransformer {

    /*
     * ViewPager2 與 ViewPager相反，越後面的頁面在越上層
     * 需設定 Z 軸順序，越接近 0 越在上層
     */

    enum class NextPlace(val place: Int) {
        TOP(-1),
        BOTTOM(1);
    }

    override fun transformPage(page: View, position: Float) {
        when{
            position <= -1f -> page.alpha = 0f
            position <= 0.0f -> {
                page.alpha = 1f - abs(position)
                page.scaleX = 1f
                page.scaleY = 1f
                page.rotation = 45 * position
                page.translationX = page.width * position
                page.translationY = -(page.height / 5 * position)
                page.translationZ = 1f - abs(position)
            }
            position <= numberOfStacked + 1 -> {
                if (position > numberOfStacked && position <= numberOfStacked + 1) {
                    page.alpha = 1f - (position - numberOfStacked)
                }else {
                    page.alpha = 1f
                }
                val scale = (page.width - mScaleOffset * position) / page.width.toFloat()
                page.scaleX = scale
                page.scaleY = scale

                page.rotation = 0f
                page.translationX = -page.width * position
                page.translationY = mScaleOffset * 1.5f * position * nextPlace.place
                page.translationZ = -1f * position
            }
            else -> page.alpha = 0f
        }
    }
}