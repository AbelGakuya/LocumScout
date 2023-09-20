package com.locums.locumscout.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.locums.locumscout.ui.shift_listing_fragments.ActiveLocumsFragment
import com.locums.locumscout.ui.shift_listing_fragments.CompletedLocumsFragment

class LocumsPagerAdapter(fragmentActivity: FragmentActivity):FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position){
            0 -> ActiveLocumsFragment()
            1 -> CompletedLocumsFragment()
            else -> throw
                    IllegalArgumentException("Invalid position: $position")
        }
    }
}