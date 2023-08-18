package com.locums.locumscout.ui.shift_listing_fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.locums.locumscout.R
import com.locums.locumscout.databinding.FragmentShiftDetailsBinding


class ShiftDetailsFragment : Fragment() {
    private lateinit var binding: FragmentShiftDetailsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shift_details, container, false)
    }

}