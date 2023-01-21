package com.schoters.android.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.schoters.android.R
import com.schoters.android.databinding.ItemNewsBinding
import com.schoters.android.db.NewsEntity
import com.schoters.android.utils.convertTo


class NewsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val itemList: ArrayList<NewsEntity> = arrayListOf()
    private var listener: NewsInterface? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return NewsViewHolder(ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as NewsViewHolder).bind(itemList[position])
    }

    override fun getItemCount(): Int = itemList.size

    fun addItem(items: ArrayList<NewsEntity>) {
        itemList.addAll(items)
        notifyItemRangeChanged(0, items.size)
    }

    fun setUpListener(listener: NewsInterface) {
        this.listener = listener
    }

    inner class NewsViewHolder(private val binding: ItemNewsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(news: NewsEntity) = with(binding) {
            ivNews.load(news.urlToImage ?: R.drawable.ic_placeholder) {
                placeholder(R.drawable.ic_placeholder)
                crossfade(true)
                listener(
                    onSuccess = { _, _ ->

                    },
                    onError = { _, _ ->
                        ivNews.load(R.drawable.ic_placeholder)
                    }
                )
            }
            tvNewsTitle.text = news.title
            tvAuthor.text = news.author ?: binding.root.context.getString(R.string.app_author)
            tvDate.text = news.publishedAt?.convertTo("dd MMM yyyy")

            var isBookmark = news.isBookmarked == 1
            if (news.isBookmarked == 1) {
                binding.ivBookmark.setImageResource(R.drawable.ic_bookmark_active)
            } else {
                binding.ivBookmark.setImageResource(R.drawable.ic_bookmark_inactive)
            }
            binding.ivBookmark.setOnClickListener {
                isBookmark = !isBookmark
                if (isBookmark) {
                    binding.ivBookmark.setImageResource(R.drawable.ic_bookmark_active)
                } else {
                    binding.ivBookmark.setImageResource(R.drawable.ic_bookmark_inactive)
                }
                listener?.onBookmarkClicked(news)
            }
            containerNews.setOnClickListener {
                listener?.onNewsClicked(news)
            }
        }
    }

    interface NewsInterface {
        fun onNewsClicked(news: NewsEntity)
        fun onBookmarkClicked(news: NewsEntity)
    }
}