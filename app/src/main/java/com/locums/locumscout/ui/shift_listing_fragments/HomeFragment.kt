package com.locums.locumscout.ui.shift_listing_fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.core.view.get
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.locums.locumscout.R
import com.locums.locumscout.adapters.HospitalsAdapter
import com.locums.locumscout.data.Doctor
import com.locums.locumscout.data.Hospital
import com.locums.locumscout.data.Profile
import com.locums.locumscout.databinding.FragmentHomeBinding
import com.locums.locumscout.other.Constants.first_name
import com.locums.locumscout.other.Constants.last_name
import com.locums.locumscout.other.Constants.name
import com.locums.locumscout.viewModels.FirebaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: FirebaseViewModel
    lateinit var auth: FirebaseAuth
    private lateinit var mDatabase1: DataSnapshot
    private lateinit var mAdapter:  HospitalsAdapter
    var hospitalArrayList: ArrayList<Hospital>? = null

   private lateinit var recyclerView: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root


        viewModel = ViewModelProvider(this).get(FirebaseViewModel::class.java)

       auth = FirebaseAuth.getInstance()
       val uid = auth.currentUser?.uid
       //getDocDetails(uid)

        GlobalScope.launch(Dispatchers.Main) {
            viewModel.fetchProfileData(uid!!)

            viewModel.profileData.observe(viewLifecycleOwner, Observer {
                profileData ->
                profileData?.let {
                    //Load and display the user's image using Glide
                    Glide.with(this@HomeFragment)
                        .load(profileData.imageUrl)
                        .error(R.drawable.baseline_lock_24)
                        .placeholder(R.drawable.baseline_person_24)
                        .into(binding.profileImage)

                    binding.hello.text = "Jambo ${profileData.name}"

                }
            })
        }



        recyclerView = binding.shiftLocumsList

//       updateUIi()

//        hospitalArrayList = ArrayList()
//        getHospitalData()
//        mAdapter = HospitalsAdapter(requireActivity(), hospitalArrayList!!)
//        recyclerView = binding.shiftLocumsList
//        recyclerView.setHasFixedSize(true)
//        recyclerView.adapter = mAdapter
       // setupRecyclerView()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }


    private fun setupRecyclerView() = binding.shiftLocumsList.apply {
        hospitalArrayList = ArrayList()
        getHospitalData()
        mAdapter = HospitalsAdapter(requireActivity(), hospitalArrayList!!)
        adapter = mAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }

    private fun getHospitalData() = CoroutineScope(Dispatchers.IO).launch {
        try {
            mDatabase1 = FirebaseDatabase.getInstance().getReference("hospital").get().await()
            for (hospitalSnapshot in mDatabase1.children) {
                val hospital = hospitalSnapshot.getValue(Hospital::class.java)
                hospitalArrayList?.add(hospital!!)
            }

            withContext(Dispatchers.Main) {
                recyclerView.adapter = mAdapter
                //     Toast.makeText(requireContext(), "Oya!!", Toast.LENGTH_SHORT).show()
                navigateToShiftDetails()
            }

        } catch (e: Exception) {

            withContext(Dispatchers.Main) {
                Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun updateUI(){
        val auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid

        if (userId != null) {
            val firestore = FirebaseFirestore.getInstance()
            val userDocument = firestore.collection("users").document(userId)

            userDocument.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userSnapshot = task.result
                    if (userSnapshot != null && userSnapshot.exists()) {
                        val username = userSnapshot.getString("username")
                        val profilePictureUrl = userSnapshot.getString("profilePictureUrl")?.toUri()

                        val text = "Hi $username"
                        binding.hello.text = text
                        binding.profileImage.setImageURI(profilePictureUrl)

                        // Update UI with retrieved data (e.g., set username and load profile picture)
                    }
                } else {
                    // Handle Firestore data retrieval failure
                }
            }
        }

    }

    private fun updateUIi(){
        val auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid

        if (userId != null) {
            CoroutineScope(Dispatchers.IO).launch {

                val firestore = FirebaseFirestore.getInstance()
                val userDocument = firestore.collection("users").document(userId)
                val userSnapshot = userDocument.get().await()
                if (userSnapshot.exists()) {
                    val username = userSnapshot.getString("username")
                    val profilePictureUrl = userSnapshot.getString("profilePictureUrl")

                    withContext(Dispatchers.Main){
                     binding.hello.text = "Hello $username"
                     binding.profileImage.setImageURI(profilePictureUrl?.toUri())
                    }

                }

                }
            }
        }


    //Getting user's username and profile image from firebase and setting them to their respective views
    private fun getUserDetails(uid: String?) = CoroutineScope(Dispatchers.IO).launch{
        try {
            mDatabase1 = FirebaseDatabase.getInstance().getReference("doctors").child(uid!!).get().await()

            val doctor = mDatabase1.getValue(Doctor::class.java)
            val firstName = doctor?.firstName
            val lastName = doctor?.lastName
            val url = doctor?.imageUrl
            withContext(Dispatchers.Main){
                if (firstName != null && lastName != null && uid != null) {
                        setPatientName(firstName,lastName)

                    binding.profileImage.setImageURI(url?.toUri())

                }
            }
            


        } catch (e: Exception){
            withContext(Dispatchers.Main){
                Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
            }
        }
    }


    private fun getDocDetails(uid: String?) {
        val userRef = FirebaseFirestore.getInstance().collection("doctor_users").document(uid!!)
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val snapshot = withContext(Dispatchers.IO) {
                    userRef.get().await()
                }
                val user = snapshot.toObject(Profile::class.java)
                if (user != null){
                    val userName = user.name
                    val imageUrl = user.imageUrl

                    //Load and display the user's image using Glide
                    Glide.with(this@HomeFragment)
                        .load(imageUrl)
                        .error(R.drawable.baseline_lock_24)
                        .placeholder(R.drawable.baseline_person_24)
                        .into(binding.profileImage)


                    binding.hello.text = "Hello $userName"
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun setPatientName(firstName: String, lastName: String) {
        name = "$firstName $lastName"
        first_name = firstName
        last_name = lastName
        binding.hello.text = "Hey $firstName"
    }




    private fun navigateToShiftDetails() {
        mAdapter.setOnItemClickListener(object : HospitalsAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                val hospital = hospitalArrayList?.get(position)
//                val token = ?.token
//                sharedViewModelToken.saveContent(token)
//                if (doctor != null) {
//                    sharedViewModel2.saveContent(doctor)
//                }
                findNavController().navigate(R.id.action_homeFragment_to_shiftListFragment)
            }
        })
    }
}


