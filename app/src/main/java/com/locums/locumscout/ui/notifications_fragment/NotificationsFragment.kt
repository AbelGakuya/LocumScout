package com.locums.locumscout.ui.notifications_fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.locums.locumscout.R
import com.locums.locumscout.databinding.FragmentNotificationsBinding


class NotificationsFragment : Fragment() {

    private lateinit var binding: FragmentNotificationsBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNotificationsBinding.inflate(inflater,container,false)
        val view = binding.root


        return view
    }


}