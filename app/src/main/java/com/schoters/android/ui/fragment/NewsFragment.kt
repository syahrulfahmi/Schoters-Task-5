package com.schoters.android.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.schoters.android.SchotersApplication
import com.schoters.android.databinding.FragmentNewsBinding
import com.schoters.android.db.NewsEntity
import com.schoters.android.network.response.News
import com.schoters.android.network.response.NewsResponse
import com.schoters.android.ui.adapter.NewsAdapter
import com.schoters.android.ui.adapter.NewsInterface
import com.schoters.android.utils.PaginationScrollListener
import com.schoters.android.viewModel.NewsViewModel
import com.schoters.android.viewModel.NewsViewModelFactory

class NewsFragment : Fragment(), NewsInterface {

    private val viewModel: NewsViewModel by viewModels {
        NewsViewModelFactory(
            requireActivity().application,
            (requireActivity().application as SchotersApplication).database.newsDao()
        )
    }

    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = NewsAdapter()
        viewModel.category = arguments?.getString(NEWS_CATEGORY) ?: ""

        getData()
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        binding.rvNews.adapter = adapter
        adapter.setUpListener(this)
        binding.rvNews.addOnScrollListener(object : PaginationScrollListener(binding.rvNews.layoutManager as LinearLayoutManager) {
            override fun isLastPage(): Boolean = viewModel.isLastPage

            override fun isLoading(): Boolean = adapter.isLoadingLoadMore

            override fun loadMoreItems() {
                adapter.isLoadingLoadMore = true
                viewModel.newsField.page += 1
                viewModel.getNews()
            }
        })
    }

    private fun getData() {
        viewModel.getNews()
        viewModel.newsResponse.observe(viewLifecycleOwner) {
            it?.let {
                viewModel.newsField.total = it.totalResults
                onSuccessGetData(it)
            } ?: run {

            }
        }
    }

    private fun onSuccessGetData(resp: NewsResponse) {
        val data = ArrayList<NewsEntity>()
        resp.articles.forEachIndexed { index, item ->
            data.add(
                NewsEntity(
                    id = index,
                    author = item.author,
                    title = item.title,
                    description = item.description,
                    url = item.url,
                    urlToImage = item.urlToImage,
                    publishedAt = item.publishedAt,
                    isBookmarked = item.isBookmarked
                )
            )
        }
        if (data.size < viewModel.newsField.perPage) {
            adapter.isLoadingLoadMore = false
            viewModel.isLastPage = true
        }
        adapter.isLoadingLoadMore = false
        adapter.addItem(data)
    }

    companion object {
        private const val NEWS_CATEGORY = "news_category"
        fun newInstance(category: String): NewsFragment {
            val fragment = NewsFragment()
            val bundle = Bundle().apply {
                putString(NEWS_CATEGORY, category)
            }
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onNewsClicked(news: News) {
        val isBookmark = if (news.isBookmarked == 1) 0 else 1
        val newsEntity = NewsEntity(
            id = (0..99).random(),
            author = news.author,
            title = news.title,
            description = news.description,
            urlToImage = news.urlToImage,
            url = news.url,
            publishedAt = news.publishedAt,
            isBookmarked = isBookmark
        )
        viewModel.insertNews(newsEntity)
    }
}