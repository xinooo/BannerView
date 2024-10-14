package com.xinooo.banner.listener

import android.content.Context
import android.view.View
import android.widget.Toast
import com.xinooo.banner.model.AdvertiseModel

class OnBannerClickListener(
    private val ctx: Context?,
    private val model: AdvertiseModel)
    : View.OnClickListener {

    override fun onClick(v: View?) {
        ctx?.let {
            Toast.makeText(it, "${model.title}: 被點擊了！", Toast.LENGTH_SHORT).show()
        }
    }

}