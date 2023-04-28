package com.kosim97.mulgaTalkTalk.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kosim97.mulgaTalkTalk.data.local.model.AutoSlideData
import com.kosim97.mulgaTalkTalk.databinding.AutoSlideLayoutBinding

class AutoSlideAdapter: ListAdapter<AutoSlideData, AutoSlideAdapter.AutoSlideViewHolder>(diffUtil) {
    inner class AutoSlideViewHolder(val binding: AutoSlideLayoutBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(currentItem: AutoSlideData) {
            binding.slideItem = currentItem
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AutoSlideViewHolder {
        val binding = AutoSlideLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AutoSlideViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AutoSlideViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun submitList(list: MutableList<AutoSlideData>?) {
        super.submitList(list)
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<AutoSlideData>(){
            override fun areItemsTheSame(oldItem: AutoSlideData, newItem: AutoSlideData): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: AutoSlideData,
                newItem: AutoSlideData
            ): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }

        }
    }
}