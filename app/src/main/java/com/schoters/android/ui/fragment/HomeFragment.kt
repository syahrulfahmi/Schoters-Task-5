package com.schoters.android.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.schoters.android.databinding.FragmentHomeBinding
import com.schoters.android.ui.adapter.ViewPagerAdapter
import com.schoters.android.utils.Constant

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpTabs()
    }

    private fun setUpTabs() {
        val fragment = LinkedHashMap<String, Fragment>()
        fragment[Constant.HEALTH] = NewsFragment.newInstance(Constant.HEALTH)
        fragment[Constant.TECH] = NewsFragment.newInstance(Constant.TECH)
        fragment[Constant.SCIENCE] = NewsFragment.newInstance(Constant.SCIENCE)
        binding.viewPager.adapter = ViewPagerAdapter(childFragmentManager, lifecycle, ArrayList(fragment.values))
        val listTitle = ArrayList(fragment.keys)
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, pos ->
            tab.text = listTitle[pos]
        }.attach()
    }
}