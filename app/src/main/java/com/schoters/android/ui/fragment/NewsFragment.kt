package com.schoters.android.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.schoters.android.databinding.FragmentNewsBinding
import com.schoters.android.db.NewsEntity
import com.schoters.android.network.Resource
import com.schoters.android.network.response.NewsResponse
import com.schoters.android.ui.adapter.NewsAdapter
import com.schoters.android.utils.isVisible
import com.schoters.android.viewModel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsFragment : Fragment() {

    private val viewModel: NewsViewModel by viewModels()

    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!

    private lateinit var newsAdapter: NewsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        newsAdapter = NewsAdapter()

        getData()
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        binding.rvNews.adapter = newsAdapter
        newsAdapter.setUpListener(object : NewsAdapter.NewsInterface {
            override fun onNewsClicked(news: NewsEntity) {
                val newsUrl = news.url.orEmpty()
                findNavController().navigate(NewsFragmentDirections.actionNewsFragmentToNewsDetailFragment(newsUrl))
            }

            override fun onBookmarkClicked(news: NewsEntity) {
                news.isBookmarked = if (news.isBookmarked == 1) 0 else 1
                viewModel.updateBookmark(news)
                newsAdapter.notifyItemChanged(0)
            }
        })
    }

    private fun getData() {
        viewModel.newsResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    val data = it.data
                    data?.let { onSuccessGetData(data) }
                    binding.contentLoadingBar.isVisible(false)
                }
                is Resource.Loading -> {
                    binding.contentLoadingBar.isVisible(true)
                }
                is Resource.Error -> {
                    Toast.makeText(binding.root.context, it.message, Toast.LENGTH_SHORT).show()
                    binding.contentLoadingBar.isVisible(false)
                }
            }
        }

    }

    private fun onSuccessGetData(resp: NewsResponse) {
        newsAdapter.addItem(ArrayList(resp.articles))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}