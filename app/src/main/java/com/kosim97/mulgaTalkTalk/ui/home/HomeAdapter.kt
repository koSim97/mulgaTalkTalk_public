package com.kosim97.mulgaTalkTalk.ui.home
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kosim97.mulgaTalkTalk.data.HomeData
import com.kosim97.mulgaTalkTalk.ui.home.detail.HomeDetailActivity
import com.kosim97.mulgaTalkTalk.databinding.HomeListItemBinding

class HomeAdapter : ListAdapter<HomeData, HomeAdapter.HomeViewHolder>(diffUtil){
    private var mContext: Context? = null
    inner class HomeViewHolder(private val binding: HomeListItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(currentItem: HomeData) {
            binding.homeItem = currentItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val binding = HomeListItemBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        mContext = parent.context
        return HomeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener {
            val intent = Intent(mContext, HomeDetailActivity::class.java).apply {
                putExtra("region", getItem(position).region)
            }
            mContext?.startActivity(intent)
        }
    }

    override fun submitList(list: MutableList<HomeData>?) {
        super.submitList(list)
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<HomeData>() {
            override fun areItemsTheSame(oldItem: HomeData, newItem: HomeData): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: HomeData, newItem: HomeData): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }

        }
    }
}
object HomeListBindingAdapter {
    @BindingAdapter("homeItem")
    @JvmStatic
    fun setItem(recyclerView: RecyclerView, item: ArrayList<HomeData>) {
        if (recyclerView.adapter == null) {
            recyclerView.adapter = HomeAdapter()
        }
        val myAdapter =recyclerView.adapter as HomeAdapter
        myAdapter.submitList(item.toMutableList())
    }
}