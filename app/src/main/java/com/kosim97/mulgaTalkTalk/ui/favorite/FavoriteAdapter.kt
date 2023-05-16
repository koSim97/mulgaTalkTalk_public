package com.kosim97.mulgaTalkTalk.ui.favorite

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kosim97.mulgaTalkTalk.data.room.RoomEntity
import com.kosim97.mulgaTalkTalk.ui.favorite.detail.FavoriteDetailActivity
import com.kosim97.mulgaTalkTalk.databinding.FavoriteListItemBinding

class FavoriteAdapter :
    PagingDataAdapter<RoomEntity, FavoriteAdapter.FavoriteViewHolder>(diffUtil) {
    private var mContext: Context? = null
    inner class FavoriteViewHolder(private val binding: FavoriteListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setItems(currentItem: RoomEntity) {
            binding.roomData = currentItem
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<RoomEntity>() {
            override fun areItemsTheSame(oldItem: RoomEntity, newItem: RoomEntity): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: RoomEntity, newItem: RoomEntity): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }
        }
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        getItem(position)?.let { holder.setItems(it) }
        holder.itemView.setOnClickListener {
            val intent = Intent(mContext, FavoriteDetailActivity::class.java).apply {
                putExtra("region", getItem(position)?.room_region)
                putExtra("product", getItem(position)?.room_product)
                putExtra("no", getItem(position)?.room_no)
            }
            mContext?.startActivity(intent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding =
            FavoriteListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        mContext = parent.context
        return FavoriteViewHolder(binding)
    }
}