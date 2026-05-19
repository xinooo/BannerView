package com.xinooo.bannerlib

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.xinooo.bannerlib.databinding.ItemBannerImageBinding

class BannerAdapter<VB : ViewDataBinding, M>(
    private val dataList: MutableList<M>,
    private val inflater: (LayoutInflater, ViewGroup, Boolean) -> VB,
    private val bind: (VB, M) -> Unit,
    private val loopPlay: Boolean
) : RecyclerView.Adapter<BannerAdapter<VB, M>.ViewHolder>() {

    val realCount: Int get() = dataList.size
    private var radius = 0f

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = inflater(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (realCount > 0) {
            holder.bind(dataList[position % realCount])
        }
    }

    override fun getItemCount(): Int {
        return if (loopPlay && realCount > 1) {
            Integer.MAX_VALUE
        } else {
            realCount
        }
    }

    fun setRadius(radius: Float): BannerAdapter<*,*> {
        this.radius = radius
        notifyDataSetChanged()
        return this
    }

    fun clear() {
        dataList.clear()
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val itemBinding: VB) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(model: M) {
            if (itemBinding is ItemBannerImageBinding && radius > 0) {
                itemBinding.cardView.radius = radius
            }
            this@BannerAdapter.bind(itemBinding, model)
        }
    }
}