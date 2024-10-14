package com.xinooo.bannerlib

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

class Adapter<VB : ViewDataBinding, M>(
    private val dataList: List<M>,
    private val inflater: (LayoutInflater, ViewGroup, Boolean) -> VB,
    private val bind: (VB, M) -> Unit,
    private val loopPlay: Boolean
) : RecyclerView.Adapter<Adapter<VB, M>.ViewHolder>() {

    private val dataSize by lazy { dataList.size }

    inner class ViewHolder(private val itemBinding: VB) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(model: M) {
            this@Adapter.bind(itemBinding, model)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = inflater(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataList[position % dataList.size])
    }

    override fun getItemCount(): Int {
        return if (loopPlay && dataSize > 1) {
            Integer.MAX_VALUE
        } else {
            dataList.size
        }
    }
}