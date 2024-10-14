package com.xinooo.bannerlib.config

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.widget.FrameLayout
import com.xinooo.bannerlib.R
import com.xinooo.bannerlib.ext.dp

class BannerConfig {
    companion object {
        /* 輪播設置 */

        //是否開啟自動輪播
        const val AUTOPLAY = true
        //是否開啟循環播放
        const val LOOP_PLAY = true
        //自動輪播的時間間隔(ms)
        const val AUTOPLAY_INTERVAL = 3000

        /* 指示器設置 */

        //是否顯示指示器
        const val SHOW_INDICATOR =  true
        //指示器背景
        val INDICATOR_BACKGROUND = ColorDrawable(Color.TRANSPARENT)
        //指示器
        val INDICATOR_DRAWABLE_RES_ID = R.drawable.selector_banner_indicator
        //指示器高度
        const val INDICATOR_HEIGHT = FrameLayout.LayoutParams.WRAP_CONTENT
        //指示器間距
        val INDICATOR_SPACING = 5.dp
        //指示器的位置
        const val INDICATOR_GRAVITY = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
        //指示器左內邊距
        val INDICATOR_PADDING_START = INDICATOR_SPACING
        //指示器右內邊距
        val INDICATOR_PADDING_END = INDICATOR_SPACING
        //指示器左外邊距
        val INDICATOR_MARGINS_TART = 16.dp
        //指示器右外邊距
        val INDICATOR_MARGIN_END = 16.dp
        //指示器上外邊距
        val INDICATOR_MARGIN_TOP = 16.dp
        //指示器下外邊距
        val INDICATOR_MARGIN_BOTTOM = 16.dp
        //是否為數位指標
        const val IS_NUMBER_INDICATOR = false
        //數字指示器文字顏色
        const val NUMBER_INDICATOR_TEXT_COLOR = Color.WHITE
        //數字指示器文字大小
        val NUMBER_INDICATOR_TEXT_SIZE = 14.dp

        /* 頁面設置 */

        //頁碼切換過程的時間長度(ms)
        const val PAGE_CHANGE_DURATION = 600
        //viewPager預加載頁面數量
        const val PAGE_LIMIT = 1
        //頁面區域距離 BannerView 頂部的距離
        const val PAGE_PADDING_TOP = 0
        //頁面區域距離 BannerView 底部的距離
        const val PAGE_PADDING_BOTTOM = 0
        //頁面區域距離 BannerView 左邊的距離
        const val PAGE_PADDING_START = 0
        //頁面區域距離 BannerView 右邊的距離
        const val PAGE_PADDING_END = 0
        //頁面區域距離 BannerView 垂直的距離
        const val PAGE_PADDING_VERTICAL = 0
        //頁面區域距離 BannerView 水平的距離
        const val PAGE_PADDING_HORIZONTAL = 0
        //頁面區域距離 BannerView 的距離
        const val PAGE_PADDING = 0
    }
}