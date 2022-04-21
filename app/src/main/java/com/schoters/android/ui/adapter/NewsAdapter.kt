package com.schoters.android.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.schoters.android.R
import com.schoters.android.databinding.ItemFooterBinding
import com.schoters.android.databinding.ItemNewsBinding
import com.schoters.android.db.NewsEntity
import com.schoters.android.network.response.News
import com.schoters.android.utils.convertTo


interface NewsInterface {
    fun onNewsClicked(news: News)
}

class NewsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val itemList: ArrayList<NewsEntity> = arrayListOf()
    var isLoadingLoadMore: Boolean = true
    private var listener: NewsInterface? = null

    companion object {
        const val LOADING_TYPE = 0
        const val ITEM_TYPE = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (isLoadingLoadMore) {
            if (position >= itemCount - 1) {
                LOADING_TYPE
            } else {
                ITEM_TYPE
            }
        } else {
            ITEM_TYPE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == LOADING_TYPE) {
            FooterViewHolder(ItemFooterBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        } else {
            NewsViewHolder(ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == LOADING_TYPE) {
            (holder as FooterViewHolder).showFooterLoading()
        } else if (getItemViewType(position) == ITEM_TYPE) {
            (holder as NewsViewHolder).bind(itemList[position])
        }
    }

    override fun getItemCount(): Int {
        return if (isLoadingLoadMore) {
            itemList.size + 1
        } else {
            itemList.size
        }
    }

    fun addItem(items: ArrayList<NewsEntity>) {
        itemList.addAll(items)
        notifyItemRangeChanged(0, items.size)
    }

    fun setUpListener(listener: NewsInterface) {
        this.listener = listener
    }

    inner class NewsViewHolder(private val binding: ItemNewsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(article: NewsEntity) = with(binding) {
            ivNews.load(article.urlToImage ?: R.drawable.ic_placeholder) {
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
            tvNewsTitle.text = article.title
            tvAuthor.text = article.author ?: binding.root.context.getString(R.string.app_author)
            tvDate.text = article.publishedAt?.convertTo("dd MMM yyyy")
        }
    }

    inner class FooterViewHolder(private val binding: ItemFooterBinding) : RecyclerView.ViewHolder(binding.root) {
        fun showFooterLoading() {
            binding.contentLoadingBar.isVisible = true
        }
    }
}