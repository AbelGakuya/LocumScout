package com.locums.locumscout.ui.shift_listing_fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
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
import com.locums.locumscout.viewModels.SharedViewModel
import com.locums.locumscout.viewModels.ShiftsViewModel
import com.locums.locumscout.viewModels.ShiftsViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class ShiftListFragment : Fragment() {

    private  var _binding: FragmentShiftListBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var mAdapter: HospitalsAdapter
    private lateinit var viewModel: FirebaseViewModel
    private lateinit var viewModelShift: ShiftsViewModel

    val sharedViewModel2: SharedViewModel by activityViewModels()
    //lateinit var auth: FirebaseAuth
   // private lateinit var mDatabase1: DataSnapshot

 //   var hospitalArrayList: ArrayList<Hospital>? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentShiftListBinding.inflate(inflater,container,false)
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
            sharedViewModel2.saveContent(selectedItem)
            findNavController().navigate(R.id.action_shiftListFragment_to_shiftDetailsFragment)
        }
        recyclerView.adapter = mAdapter
        // Show ProgressBar while loading data
        val emptyStateLayout = view.findViewById<View>(R.id.emptyStateLayout)
        binding.loadingIndicator.visibility = View.VISIBLE

        viewModelShift.firebaseData.observe(
            viewLifecycleOwner){
                data ->
            binding.loadingIndicator.visibility = View.GONE
            if (data.isEmpty()) {
                // No data available, show the placeholder
                recyclerView.visibility = View.GONE
                emptyStateLayout.visibility = View.VISIBLE
            } else {
                // Data is available, show the RecyclerView
                recyclerView.visibility = View.VISIBLE
                emptyStateLayout.visibility = View.GONE
                mAdapter.updateData(data)
            }
        }

        viewModelShift.fetchFirebaseData()


//        auth = FirebaseAuth.getInstance()
//        val uid = auth.currentUser?.uid
//        //getDocDetails(uid)


    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }



    override fun onPause() {
        super.onPause()
        _binding = null
    }

    override fun onStop() {
        super.onStop()
        _binding = null
    }



//    private fun setupRecyclerView() = binding.locumsList.apply {
//        // runAdapter = RunAdapter()
//        //  adapter = runAdapter
//        layoutManager = LinearLayoutManager(requireContext())
//    }


}