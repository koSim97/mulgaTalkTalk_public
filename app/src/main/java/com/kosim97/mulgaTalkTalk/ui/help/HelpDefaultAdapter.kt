package com.kosim97.mulgaTalkTalk.ui.help

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kosim97.mulgaTalkTalk.data.local.model.HelpData
import com.kosim97.mulgaTalkTalk.databinding.HelpListItemBinding

class HelpAdapter: androidx.recyclerview.widget.ListAdapter<HelpData, HelpAdapter.HelpViewHolder>(diffUtil) {

    inner class HelpViewHolder(private val binding: HelpListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun bind(currentItem: HelpData) {
                binding.data = currentItem
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HelpViewHolder {
        val binding = HelpListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HelpViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HelpViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun submitList(list: MutableList<HelpData>?) {
        super.submitList(list)
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<HelpData>(){
            override fun areItemsTheSame(oldItem: HelpData, newItem: HelpData): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: HelpData, newItem: HelpData): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }
        }
    }
}

object HelpBindingAdapter {
    @BindingAdapter("helpItem")
    @JvmStatic
    fun setHelpItem(recyclerView: RecyclerView, item: ArrayList<HelpData>) {
        if (recyclerView.adapter == null) {
            recyclerView.adapter = HelpAdapter()
        }

        val myAdapter = recyclerView.adapter as HelpAdapter
        myAdapter.submitList(item.toMutableList())
    }
}