package com.locums.locumscout.ui.shift_listing_fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.locums.locumscout.R
import com.locums.locumscout.adapters.CompletedLocumsAdapter
import com.locums.locumscout.databinding.FragmentCompletedLocumsBinding
import com.locums.locumscout.repo.FirebaseRepository
import com.locums.locumscout.viewModels.ShiftsViewModel
import com.locums.locumscout.viewModels.ShiftsViewModelFactory
import com.locums.locumscouth.adapters.ActiveLocumsAdapter


class CompletedLocumsFragment : Fragment() {
    private lateinit var binding: FragmentCompletedLocumsBinding

    private lateinit var viewModel: ShiftsViewModel

    private lateinit var recyclerView: RecyclerView

    private lateinit var mAdapter: CompletedLocumsAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCompletedLocumsBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repository = FirebaseRepository()
        // viewModel = ViewModelProvider(this).get(LocumsViewModel::class.java)
        viewModel = ViewModelProvider(this, ShiftsViewModelFactory(repository)).get(ShiftsViewModel::class.java)

        recyclerView = binding.activeLocumsList
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        mAdapter = CompletedLocumsAdapter{
                selectedItem ->
            // sharedViewModel2.saveContent(selectedItem)
            //   findNavController().navigate(R.id.action_shiftListFragment_to_shiftDetailsFragment)
        }
        recyclerView.adapter = mAdapter


        val uid = FirebaseAuth.getInstance().currentUser?.uid
        viewModel.fetchCompletedLocums(uid)

        viewModel.completedLocumsLiveData.observe(
            viewLifecycleOwner){
                data ->
            mAdapter.updateActiveLocums(data)
        }
    }


}