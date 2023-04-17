package com.kosim97.mulgaTalkTalk.ui.home.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kosim97.mulgaTalkTalk.data.Detail
import com.kosim97.mulgaTalkTalk.databinding.CommonListItemBinding

class HomeDetailAdapter: PagingDataAdapter<Detail, HomeDetailAdapter.HomeDetailViewHolder>(diffUtil) {

    inner class HomeDetailViewHolder(private val binding: CommonListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItem(currentItem: Detail) {
            binding.detailItem = currentItem
        }
    }
    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Detail>() {
            override fun areItemsTheSame(oldItem: Detail, newItem: Detail): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Detail, newItem: Detail): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }
        }
    }

    override fun onBindViewHolder(holder: HomeDetailViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bindItem(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeDetailViewHolder {
        val binding = CommonListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeDetailViewHolder(binding = binding)
    }
}