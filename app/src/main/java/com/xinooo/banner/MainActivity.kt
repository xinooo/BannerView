package com.xinooo.banner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.xinooo.banner.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityMainBinding?>(this, R.layout.activity_main)
            .apply {
                btnLandScape.setOnClickListener { startActivity(Intent(this@MainActivity, LandScapeBannerActivity::class.java)) }
                btnPortrait.setOnClickListener { startActivity(Intent(this@MainActivity, PortraitBannerActivity::class.java)) }
            }
    }

}