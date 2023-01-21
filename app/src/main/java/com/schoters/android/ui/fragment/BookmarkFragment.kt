package com.schoters.android.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.schoters.android.databinding.FragmentBookmarkBinding
import com.schoters.android.db.NewsEntity
import com.schoters.android.ui.adapter.NewsAdapter
import com.schoters.android.viewModel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class BookmarkFragment : Fragment() {

    private var _binding: FragmentBookmarkBinding? = null
    private val binding get() = _binding!!
    private lateinit var newsAdapter: NewsAdapter
//    private val viewModel: NewsViewModel by viewModels {
//        NewsViewModelFactory(
//            requireActivity().application,
//            (requireActivity().application as SchotersApplication).database.newsDao()
//        )
//    }
    private val viewModel : NewsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookmarkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
    }

    @SuppressLint("SetJavaScriptEnabled", "ClickableViewAccessibility")
    private fun setupView() {
        newsAdapter = NewsAdapter()
        viewModel.getBookmarkedNews(::onSuccess)
    }

    private fun onSuccess(news: List<NewsEntity>) {
        newsAdapter.addItem(ArrayList(news))
        newsAdapter.setUpListener(object : NewsAdapter.NewsInterface{
            override fun onNewsClicked(news: NewsEntity) {
                val newsUrl = news.url.orEmpty()
                findNavController().navigate(BookmarkFragmentDirections.actionNewsBookmarkToNewsDetailFragment(newsUrl))
            }

            override fun onBookmarkClicked(news: NewsEntity) {
                news.isBookmarked = if (news.isBookmarked == 1) 0 else 1
                viewModel.updateBookmark(news)
                newsAdapter.notifyItemChanged(0)
            }
        })
        with(binding) {
            rvBookmarkedNews.adapter = newsAdapter
            if (news.isEmpty()) {
                tvNoData.isVisible = true
                rvBookmarkedNews.isVisible = false
            } else {
                tvNoData.isVisible = false
                rvBookmarkedNews.isVisible = true
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {}
}