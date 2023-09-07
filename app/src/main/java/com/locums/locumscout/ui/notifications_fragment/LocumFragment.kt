package com.locums.locumscout.ui.notifications_fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.locums.locumscout.R
import com.locums.locumscout.databinding.FragmentLocumBinding
import com.locums.locumscout.other.Constants
import com.locums.locumscout.viewModels.SharedNotificationsViewModel
import com.locums.locumscout.viewModels.SharedViewModel


class LocumFragment : Fragment() {

    private lateinit var binding: FragmentLocumBinding


    val sharedViewModel2: SharedNotificationsViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLocumBinding.inflate(inflater,container,false)
        val view = binding.root

        sharedViewModel2.content.observe(viewLifecycleOwner, {
                content ->
           binding.messageTextView.text = content.message
            binding.titleTextView.text = content.title
            binding.timeTextView.text = content.timeStamp.toString()
        })

        return view
    }


}