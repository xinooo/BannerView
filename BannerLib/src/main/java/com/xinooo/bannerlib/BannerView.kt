package com.xinooo.bannerlib

import android.animation.Animator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.*
import androidx.core.view.children
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.xinooo.bannerlib.config.BannerConfig
import com.xinooo.bannerlib.databinding.ItemBannerImageBinding
import kotlinx.coroutines.*

class BannerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr), DefaultLifecycleObserver {

    private val TAG = "BannerView"

    private var autoplay = BannerConfig.AUTOPLAY
    private var loopPlay = BannerConfig.LOOP_PLAY
    private var autoplayInterval = BannerConfig.AUTOPLAY_INTERVAL

    private var showIndicator = BannerConfig.SHOW_INDICATOR
    private var indicatorBackground: Drawable = BannerConfig.INDICATOR_BACKGROUND
    private var indicatorDrawableResId = BannerConfig.INDICATOR_DRAWABLE_RES_ID
    private var indicatorHeight = BannerConfig.INDICATOR_HEIGHT
    private var indicatorSpacing = BannerConfig.INDICATOR_SPACING
    private var indicatorGravity = BannerConfig.INDICATOR_GRAVITY
    private var indicatorPaddingStart = BannerConfig.INDICATOR_PADDING_START
    private var indicatorPaddingEnd = BannerConfig.INDICATOR_PADDING_END
    private var indicatorMarginStart = BannerConfig.INDICATOR_MARGINS_TART
    private var indicatorMarginEnd = BannerConfig.INDICATOR_MARGIN_END
    private var indicatorMarginTop = BannerConfig.INDICATOR_MARGIN_TOP
    private var indicatorMarginBottom = BannerConfig.INDICATOR_MARGIN_BOTTOM

    private var isNumberIndicator = BannerConfig.IS_NUMBER_INDICATOR
    private var numberIndicatorTextColor = BannerConfig.NUMBER_INDICATOR_TEXT_COLOR
    private var numberIndicatorTextSize = BannerConfig.NUMBER_INDICATOR_TEXT_SIZE
    private var pageChangeDuration = BannerConfig.PAGE_CHANGE_DURATION
    private var pageLimit = BannerConfig.PAGE_LIMIT.toInt()
    private var pagePaddingTop = BannerConfig.PAGE_PADDING_TOP
    private var pagePaddingBottom = BannerConfig.PAGE_PADDING_BOTTOM
    private var pagePaddingStart = BannerConfig.PAGE_PADDING_START
    private var pagePaddingEnd = BannerConfig.PAGE_PADDING_END
    private var pagePaddingVertical = BannerConfig.PAGE_PADDING_VERTICAL
    private var pagePaddingHorizontal = BannerConfig.PAGE_PADDING_HORIZONTAL
    private var pagePadding = BannerConfig.PAGE_PADDING

    private val viewPager: ViewPager2
    private var adapter: Adapter<*, *>? = null
    private lateinit var indicatorParent: LinearLayout
    private lateinit var numberTv: TextView
    private val viewScope = MainScope()
    private var autoplayJob: Job? = null
    private var allowUserScrollable = true
    private var dataSize = 0

    init {
        initCustomAttrs(context, attrs)
        viewPager = ViewPager2(context)
        viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        viewPager.offscreenPageLimit = pageLimit
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                switchIndicator(position)
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }
        })
        val vpView: View = viewPager.getChildAt(0)
        if (vpView is RecyclerView) {
            when {
                pagePadding != 0 -> vpView.setPaddingRelative(pagePadding, pagePadding, pagePadding, pagePadding)
                pagePaddingHorizontal != 0 || pagePaddingVertical != 0 -> {
                    if (pagePaddingVertical == 0) {
                        vpView.setPaddingRelative(pagePaddingHorizontal, pagePaddingTop, pagePaddingHorizontal, pagePaddingBottom)
                    }else if (pagePaddingHorizontal == 0){
                        vpView.setPaddingRelative(pagePaddingStart, pagePaddingVertical, pagePaddingEnd, pagePaddingVertical)
                    }else {
                        vpView.setPaddingRelative(pagePaddingHorizontal, pagePaddingVertical, pagePaddingHorizontal, pagePaddingVertical)
                    }
                }
                else -> vpView.setPaddingRelative(pagePaddingStart, pagePaddingTop, pagePaddingEnd, pagePaddingBottom)
            }
            vpView.clipToPadding = false
        }
        val viewPageLp = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        addView(viewPager, viewPageLp)
        if (showIndicator) {
            initIndicatorParent()
        }
    }

    private fun initCustomAttrs(context: Context, attrs: AttributeSet?) {
        attrs ?: return
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.BannerView)
        autoplay = typedArray.getBoolean(R.styleable.BannerView_autoplay, autoplay)
        loopPlay = typedArray.getBoolean(R.styleable.BannerView_loopPlay, loopPlay)
        autoplayInterval = typedArray.getInt(R.styleable.BannerView_autoplayInterval, autoplayInterval)

        showIndicator = typedArray.getBoolean(R.styleable.BannerView_showIndicator, showIndicator)
        indicatorBackground = typedArray.getDrawable(R.styleable.BannerView_indicatorBackground) ?: indicatorBackground
        indicatorDrawableResId = typedArray.getResourceId(R.styleable.BannerView_indicatorDrawable, indicatorDrawableResId)
        indicatorHeight = typedArray.getLayoutDimension(R.styleable.BannerView_indicatorHeight, indicatorHeight)
        indicatorSpacing = typedArray.getDimensionPixelSize(R.styleable.BannerView_indicatorSpacing, indicatorSpacing)
        indicatorGravity = typedArray.getInt(R.styleable.BannerView_indicatorGravity, indicatorGravity)
        indicatorPaddingStart = typedArray.getDimensionPixelSize(R.styleable.BannerView_indicatorPaddingStart, indicatorPaddingStart)
        indicatorPaddingEnd = typedArray.getDimensionPixelSize(R.styleable.BannerView_indicatorPaddingEnd, indicatorPaddingEnd)
        indicatorMarginStart = typedArray.getDimensionPixelSize(R.styleable.BannerView_indicatorMarginStart, indicatorMarginStart)
        indicatorMarginEnd = typedArray.getDimensionPixelSize(R.styleable.BannerView_indicatorMarginEnd, indicatorMarginEnd)
        indicatorMarginTop = typedArray.getDimensionPixelSize(R.styleable.BannerView_indicatorMarginTop, indicatorMarginTop)
        indicatorMarginBottom = typedArray.getDimensionPixelSize(R.styleable.BannerView_indicatorMarginBottom, indicatorMarginBottom)
        isNumberIndicator = typedArray.getBoolean(R.styleable.BannerView_isNumberIndicator, isNumberIndicator)
        numberIndicatorTextColor = typedArray.getColor(R.styleable.BannerView_numberIndicatorTextColor, numberIndicatorTextColor)
        numberIndicatorTextSize = typedArray.getDimensionPixelSize(R.styleable.BannerView_numberIndicatorTextSize, numberIndicatorTextSize)

        pageChangeDuration = typedArray.getInt(R.styleable.BannerView_pageChangeDuration, pageChangeDuration)
        pageLimit = typedArray.getInt(R.styleable.BannerView_pageLimit, pageLimit)
        pagePaddingTop = typedArray.getDimensionPixelSize(R.styleable.BannerView_pagePaddingTop, pagePaddingTop)
        pagePaddingBottom = typedArray.getDimensionPixelSize(R.styleable.BannerView_pagePaddingBottom, pagePaddingBottom)
        pagePaddingStart = typedArray.getDimensionPixelSize(R.styleable.BannerView_pagePaddingStart, pagePaddingStart)
        pagePaddingEnd = typedArray.getDimensionPixelSize(R.styleable.BannerView_pagePaddingEnd, pagePaddingEnd)
        pagePaddingVertical = typedArray.getDimensionPixelSize(R.styleable.BannerView_pagePaddingVertical, pagePaddingVertical)
        pagePaddingHorizontal = typedArray.getDimensionPixelSize(R.styleable.BannerView_pagePaddingHorizontal, pagePaddingHorizontal)
        pagePadding = typedArray.getDimensionPixelSize(R.styleable.BannerView_pagePadding, pagePadding)

        typedArray.recycle()
    }

    //初始化指示器
    private fun initIndicatorParent() {
        val h = when(indicatorHeight) {
            -1 -> LayoutParams.MATCH_PARENT
            -2 -> LayoutParams.WRAP_CONTENT
            else -> indicatorHeight
        }
        val indicatorLp = LayoutParams(LayoutParams.WRAP_CONTENT, h).apply {
            gravity = indicatorGravity
            marginStart = indicatorMarginStart
            marginEnd = indicatorMarginEnd
            topMargin = indicatorMarginTop
            bottomMargin = indicatorMarginBottom
        }
        indicatorParent = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
            setPaddingRelative(indicatorPaddingStart, 0, indicatorPaddingEnd, 0)
            background = indicatorBackground
        }
        addView(indicatorParent, indicatorLp)
    }

    fun <M> setData(dataList: List<M>, bind: (ItemBannerImageBinding, M) -> Unit) {
        setData(dataList, ItemBannerImageBinding::inflate, bind)
    }

    fun <VB: ViewDataBinding, M> setData(
        dataList: List<M>,
        inflater: (LayoutInflater, ViewGroup, Boolean) -> VB,
        bind: (VB, M) -> Unit) {
        dataSize = dataList.size
        adapter = Adapter(dataList, inflater, bind, loopPlay)
        viewPager.adapter = adapter
        if (showIndicator) {
            updateIndicator()
        }
        if (dataSize > 0) {
            val firstItem: Int = if (loopPlay) Int.MAX_VALUE / 2 - Int.MAX_VALUE / 2 % dataList.size else 0
            viewPager.setCurrentItem(firstItem, false)
            switchIndicator(firstItem)
        }
        if (autoplay) {
            startAutoplay()
        }
    }

    //更新指示器數量
    private fun updateIndicator() {
        indicatorParent.removeAllViews()
        if (!isNumberIndicator) {
            for (i in 0 until dataSize) {
                val imageView = ImageView(context).apply {
                    setImageResource(indicatorDrawableResId)
                }
                indicatorParent.addView(imageView, getIndicatorLayoutParams(i, dataSize))
            }
        } else {
            numberTv = TextView(context).apply {
                setTextColor(numberIndicatorTextColor)
                setTextSize(TypedValue.COMPLEX_UNIT_PX, numberIndicatorTextSize.toFloat())
            }
            indicatorParent.addView(numberTv, LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT))
        }
    }

    private fun getIndicatorLayoutParams(position: Int, size: Int) =
        LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            .apply {
                when (position) {
                    size - 1 -> {
                        setMargins(0, 0, 0, 0)
                    }
                    else -> {
                        setMargins(0, 0, indicatorSpacing, 0)
                    }
                }
            }


    //切換指示器選中項
    @SuppressLint("SetTextI18n")
    private fun switchIndicator(position: Int) {
        if (showIndicator && adapter != null) {
            val realPosition = position % dataSize
            if (isNumberIndicator) {
                numberTv.text = "${realPosition + 1}/$dataSize"
            } else {
                indicatorParent.children.forEachIndexed { index, child ->
                    child.isSelected = index == realPosition
                    child.layoutParams = getIndicatorLayoutParams(index, dataSize)
                    child.invalidate()
                }
            }
        }
    }

    fun setAutoplay(autoplay: Boolean) {
        this.autoplay = autoplay
        if (autoplay) {
            startAutoplay()
        } else {
            stopAutoplay()
        }
    }

    fun setCurrentItem(item: Int) {
        if (adapter == null) {
            return
        }
        if (loopPlay) {
            val realCurrentItem: Int = viewPager.currentItem
            val currentItem: Int = realCurrentItem % dataSize
            val offset = item - currentItem
            viewPager.setCurrentItem(realCurrentItem + offset, false)
        } else {
            viewPager.setCurrentItem(item, false)
        }
    }

    fun setAllowUserScrollable(scrollable: Boolean): BannerView {
        viewPager.isUserInputEnabled = scrollable
        allowUserScrollable = scrollable
        return this
    }

    fun setPageOverScrollMode(overScrollMode: Int): BannerView {
        viewPager.overScrollMode = overScrollMode
        return this
    }

    fun addOnPageChangeCallback(callback: ViewPager2.OnPageChangeCallback): BannerView {
        viewPager.registerOnPageChangeCallback(callback)
        return this
    }

    fun removeOnPageChangeCallback(callback: ViewPager2.OnPageChangeCallback): BannerView {
        viewPager.unregisterOnPageChangeCallback(callback)
        return this
    }

    fun setPageTransformer(transformer: ViewPager2.PageTransformer?): BannerView {
        viewPager.setPageTransformer(transformer)
        return this
    }

    private var isObserverAdded = false
    fun addBannerLifecycleObserver(owner: LifecycleOwner?): BannerView {
        if (owner != null && !isObserverAdded) {
            owner.lifecycle.addObserver(this)
            isObserverAdded = true
        }
        return this
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        Log.i(TAG, "onStart")
        if (autoplay) {
            startAutoplay()
        }
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        Log.i(TAG, "onStop")
        stopAutoplay()
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (autoplay && allowUserScrollable) {
            when (ev.action) {
                MotionEvent.ACTION_DOWN -> stopAutoplay()
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> startAutoplay()
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun startAutoplay() {
        if (adapter == null || dataSize <= 1 || visibility != VISIBLE) {
            return
        }
        stopAutoplay()
        autoplayJob = viewScope.launch {
            while (isActive) {
                delay(autoplayInterval.toLong())
                //無開啟循環播放且播放到做後一張時，跳回第一張繼續播放
                if (!loopPlay && viewPager.currentItem == dataSize - 1) {
                    viewPager.setCurrentItem(0, false)
                    continue
                }
                val item = viewPager.currentItem + 1
                val pagePxWidth = when {
                    pagePadding != 0 -> viewPager.width - pagePadding * 2
                    pagePaddingHorizontal != 0 -> viewPager.width - pagePaddingHorizontal * 2
                    else -> viewPager.width - pagePaddingStart - pagePaddingEnd
                }
                viewPager.setCurrentItemWithAnim(item, pageChangeDuration.toLong(), pagePxWidth)
                switchIndicator(item)
            }
        }
    }

    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        if (autoplay) {
            if (visibility == VISIBLE) {
                startAutoplay()
            } else {
                stopAutoplay()
            }
        }
    }

    private fun stopAutoplay() {
        autoplayJob?.cancel()
        autoplayJob = null
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopAutoplay()
        viewScope.cancel()
    }

    private var previousValue = 0
    private val pageChangeAnimator = ValueAnimator().apply {
        addUpdateListener { valueAnimator ->
            val currentValue = valueAnimator.animatedValue as Int
            val currentPxToDrag = (currentValue - previousValue).toFloat()
            viewPager.fakeDragBy(-currentPxToDrag)
            previousValue = currentValue
        }
        addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                previousValue = 0
                viewPager.beginFakeDrag()
            }

            override fun onAnimationEnd(animation: Animator) {
                viewPager.endFakeDrag()
            }

            override fun onAnimationCancel(animation: Animator) {
                viewPager.endFakeDrag()
            }

            override fun onAnimationRepeat(animation: Animator) {}
        })
        interpolator = AccelerateDecelerateInterpolator()
    }

    private fun ViewPager2.setCurrentItemWithAnim(
        item: Int,
        duration: Long,
        pagePxWidth: Int = width,
    ) {
        if (pagePxWidth <= 0) {
            return
        }
        val pxToDrag: Int = pagePxWidth * (item - currentItem)
        pageChangeAnimator.setIntValues(0, pxToDrag)
        pageChangeAnimator.duration = duration
        pageChangeAnimator.start()
    }

}