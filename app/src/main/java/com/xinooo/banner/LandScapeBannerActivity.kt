package com.xinooo.banner

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.xinooo.banner.databinding.ActivityLandscapeBannerBinding
import com.xinooo.banner.databinding.ItemRoundBannerBinding
import com.xinooo.banner.listener.OnBannerClickListener
import com.xinooo.banner.model.AdvertiseModel
import com.xinooo.bannerlib.transformer.*

class LandScapeBannerActivity : AppCompatActivity(){
    private lateinit var binding: ActivityLandscapeBannerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = "LandScapeBanner"
        binding = DataBindingUtil.setContentView(this, R.layout.activity_landscape_banner)

        val adList = ArrayList<AdvertiseModel>().apply {
            add(AdvertiseModel("Advertise_001", R.mipmap.banner_a1))
            add(AdvertiseModel("Advertise_002", pic_url = "https://qiniu.hteew.com/upload/2024926/1727319258331.jpg"))
            add(AdvertiseModel("Advertise_003", R.mipmap.banner_a2))
            add(AdvertiseModel("Advertise_004", pic_url = "https://qiniu.hteew.com/upload/2024724/1721809488962.jpg"))
            add(AdvertiseModel("Advertise_005", R.mipmap.banner_a3))
            add(AdvertiseModel("Advertise_006", pic_url = "https://qiniu.hteew.com/upload/2024820/1724144572220.jpg"))
            add(AdvertiseModel("Advertise_007", R.mipmap.banner_a4))
        }

        binding.banner1.addBannerLifecycleObserver(this)
            .setPageTransformer(CubeOutTransformer())
            .setData(adList, ItemRoundBannerBinding::inflate) { bannerBinding, data ->
                setImg(bannerBinding.img, data)
                bannerBinding.img.setOnClickListener(OnBannerClickListener(this, data))
            }

        binding.banner2.addBannerLifecycleObserver(this)
            .setPageTransformer(FadePageTransformer())
            .setData(adList) { bannerBinding, data ->
                setImg(bannerBinding.img, data)
                bannerBinding.img.setOnClickListener(OnBannerClickListener(this, data))
            }

        binding.banner3.addBannerLifecycleObserver(this)
            .setPageTransformer(FlipPageTransformer())
            .setData(adList) { bannerBinding, data ->
                setImg(bannerBinding.img, data)
                bannerBinding.img.setOnClickListener(OnBannerClickListener(this, data))
            }

        binding.banner4.addBannerLifecycleObserver(this)
            .setPageTransformer(ParallaxTransformer())
            .setData(adList) { bannerBinding, data ->
                setImg(bannerBinding.img, data)
                bannerBinding.img.setOnClickListener(OnBannerClickListener(this, data))
            }

        binding.banner5.addBannerLifecycleObserver(this)
            .setPageTransformer(StackTransformer())
            .setData(adList) { bannerBinding, data ->
                setImg(bannerBinding.img, data)
                bannerBinding.img.setOnClickListener(OnBannerClickListener(this, data))
            }

        binding.banner6.addBannerLifecycleObserver(this)
            .setPageTransformer(ZoomPageTransformer())
            .setData(adList) { bannerBinding, data ->
                setImg(bannerBinding.img, data)
                bannerBinding.img.setOnClickListener(OnBannerClickListener(this, data))
            }

        binding.banner7.addBannerLifecycleObserver(this)
            .setPageTransformer(CardTransformer())
            .setData(adList, ItemRoundBannerBinding::inflate) { bannerBinding, data ->
                setImg(bannerBinding.img, data)
                bannerBinding.img.setOnClickListener(OnBannerClickListener(this, data))
            }
    }

    private fun setImg(img: ImageView, model: AdvertiseModel) {
        img.scaleType = ImageView.ScaleType.FIT_XY
        if (model.pic_id != 0) {
            img.setImageResource(model.pic_id)
        } else {
            Glide.with(this).load(model.pic_url).into(img)
        }
    }

}