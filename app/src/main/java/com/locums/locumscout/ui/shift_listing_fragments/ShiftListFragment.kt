package com.locums.locumscout.ui.shift_listing_fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.locums.locumscout.R
import com.locums.locumscout.adapters.HospitalsAdapter
import com.locums.locumscout.data.Hospital
import com.locums.locumscout.databinding.FragmentHomeBinding
import com.locums.locumscout.databinding.FragmentShiftListBinding
import com.locums.locumscout.repo.FirebaseRepository
import com.locums.locumscout.viewModels.FirebaseViewModel
import com.locums.locumscout.viewModels.ShiftsViewModel
import com.locums.locumscout.viewModels.ShiftsViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class ShiftListFragment : Fragment() {

    private lateinit var binding: FragmentShiftListBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var mAdapter: HospitalsAdapter
    private lateinit var viewModel: FirebaseViewModel
    private lateinit var viewModelShift: ShiftsViewModel
    //lateinit var auth: FirebaseAuth
   // private lateinit var mDatabase1: DataSnapshot

 //   var hospitalArrayList: ArrayList<Hospital>? = null


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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repository = FirebaseRepository()
        viewModel = ViewModelProvider(this).get(FirebaseViewModel::class.java)
        viewModelShift = ViewModelProvider(this, ShiftsViewModelFactory(repository)).get(ShiftsViewModel::class.java)
        recyclerView = binding.locumsList
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        mAdapter = HospitalsAdapter{
                selectedItem ->
            findNavController().navigate(R.id.action_shiftListFragment_to_shiftDetailsFragment)
        }
        recyclerView.adapter = mAdapter

        viewModelShift.firebaseData.observe(
            viewLifecycleOwner){
                data ->
            mAdapter.updateData(data)
        }

        viewModelShift.fetchFirebaseData()


//        auth = FirebaseAuth.getInstance()
//        val uid = auth.currentUser?.uid
//        //getDocDetails(uid)


    }

//    private fun setupRecyclerView() = binding.locumsList.apply {
//        // runAdapter = RunAdapter()
//        //  adapter = runAdapter
//        layoutManager = LinearLayoutManager(requireContext())
//    }


}