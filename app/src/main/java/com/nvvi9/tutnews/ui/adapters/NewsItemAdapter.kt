package com.nvvi9.tutnews.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nvvi9.tutnews.R
import com.nvvi9.tutnews.databinding.NewsItemBinding
import com.nvvi9.tutnews.ui.listeners.NewsItemListener
import com.nvvi9.tutnews.vo.NewsItem

class NewsItemAdapter(private val newsItemListener: NewsItemListener) :
    ListAdapter<NewsItem, NewsItemAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder.create(parent, newsItemListener)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    class ViewHolder private constructor(
        private val binding: NewsItemBinding,
        newsItemListener: NewsItemListener
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.listener = newsItemListener
        }

        private val viewedCornerRadius =
            itemView.resources.getDimension(R.dimen.viewed_corner_radius)

        fun bind(item: NewsItem) {
            binding.run {
                newsItem = item
                updateCardViewTopLeftCornerRadius(if (item.isViewed) 1f else 0f)
                executePendingBindings()
            }
        }

        private fun updateCardViewTopLeftCornerRadius(interpolation: Float) {
            binding.cardView.run {
                shapeAppearanceModel = shapeAppearanceModel.toBuilder()
                    .setTopLeftCornerSize(interpolation * viewedCornerRadius)
                    .build()
            }
        }

        companion object {
            fun create(parent: ViewGroup, newsItemListener: NewsItemListener) =
                ViewHolder(
                    NewsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                    newsItemListener
                )
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<NewsItem>() {

        override fun areItemsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean =
            oldItem == newItem
    }
}