package com.locums.locumscout.ui.shift_listing_fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.locums.locumscout.R
import com.locums.locumscout.databinding.FragmentShiftListBinding


class ShiftListFragment : Fragment() {

    private lateinit var binding: FragmentShiftListBinding
    private lateinit var recyclerView: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentShiftListBinding.inflate(inflater,container,false)
        val view = binding.root

        recyclerView = binding.locumsList


        //setupRecyclerView()


        return view
    }

    private fun setupRecyclerView() = binding.locumsList.apply {
        // runAdapter = RunAdapter()
        //  adapter = runAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }


}