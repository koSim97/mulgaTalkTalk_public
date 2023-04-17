package com.kosim97.mulgaTalkTalk.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kosim97.mulgaTalkTalk.data.Detail
import com.kosim97.mulgaTalkTalk.databinding.CommonListItemBinding


class SearchAdapter: PagingDataAdapter<Detail, SearchAdapter.SearchViewHolder>(diffUtil) {
    inner class SearchViewHolder(private val binding: CommonListItemBinding): RecyclerView.ViewHolder(
        binding.root) {
        fun setSearchItems(currentItem: Detail) {
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

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        getItem(position)?.let { holder.setSearchItems(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding = CommonListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(binding)
    }
}