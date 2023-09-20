package com.locums.locumscout.ui.shift_listing_fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.locums.locumscout.R
import com.locums.locumscout.adapters.LocumsPagerAdapter
import com.locums.locumscout.databinding.FragmentOrdersBinding


class OrdersFragment : Fragment() {

    private lateinit var binding: FragmentOrdersBinding

    private lateinit var tabLayout: TabLayout

    private lateinit var viewPager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOrdersBinding.inflate(inflater,container,false)
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tabLayout = binding.tabs

        viewPager = binding.viewPager

        val tabSelectedIndicator = ContextCompat.getDrawable(requireContext(),
            R.drawable.custom_tab_selected_indicator)

        tabLayout.setSelectedTabIndicator(tabSelectedIndicator)

        val adapter = LocumsPagerAdapter(requireActivity())

        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout,viewPager){
            tab, position ->
            tab.text = when (position){
                0 -> "Active Locums"
                1 -> "Completed Locums"

                else -> ""
            }
        }.attach()

    }


}