package com.locums.locumscout.ui.shift_listing_fragments

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.core.view.get
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.locums.locumscout.R
import com.locums.locumscout.adapters.HospitalsAdapter
import com.locums.locumscout.adapters.TestAdapter
import com.locums.locumscout.data.Abel
import com.locums.locumscout.data.Doctor
import com.locums.locumscout.data.Hospital
import com.locums.locumscout.data.Profile
import com.locums.locumscout.databinding.FragmentHomeBinding
import com.locums.locumscout.other.Constants.first_name
import com.locums.locumscout.other.Constants.last_name
import com.locums.locumscout.other.Constants.name
import com.locums.locumscout.repo.FirebaseRepository
import com.locums.locumscout.viewModels.FirebaseViewModel
import com.locums.locumscout.viewModels.ShiftsViewModel
import com.locums.locumscout.viewModels.ShiftsViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class HomeFragment : Fragment() {

    private lateinit var handler: Handler
    private var currentPosition = 0
    private val scrollDelay = 3000L



    private  var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: FirebaseViewModel
    private lateinit var viewModelShift: ShiftsViewModel
    private lateinit var mAdapter:  HospitalsAdapter
    lateinit var auth: FirebaseAuth
    private lateinit var mDatabase1: DataSnapshot
    private lateinit var recyclerView: RecyclerView


    var hospitalArrayList: ArrayList<Hospital>? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root







//       updateUIi()

//        hospitalArrayList = ArrayList()
//     //   getHospitalData()
//
//
//        val abelzArrayList : ArrayList<Abel> = ArrayList()
//        val firstName = "Abel"
//        val lastName = "Mwas"
//        val abel = Abel(firstName, lastName)
//        val first = "Jonte"
//        val last = "Gaks"
//        val jon = Abel(first,last)
//        abelzArrayList.add(jon)
//        abelzArrayList.add(abel)
//        testAdapter = TestAdapter(abelzArrayList)
//
//     //   mAdapter = HospitalsAdapter(requireActivity(), hospitalArrayList!!)
//
//        recyclerView = binding.shiftLocumsList
//        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(requireContext())
//        recyclerView.adapter = testAdapter
       // setupRecyclerView()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //setupRecyclerView()

        val repository = FirebaseRepository()
        viewModel = ViewModelProvider(this).get(FirebaseViewModel::class.java)
        viewModelShift = ViewModelProvider(this, ShiftsViewModelFactory(repository)).get(ShiftsViewModel::class.java)
        recyclerView = binding.shiftLocumsList
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)

        mAdapter = HospitalsAdapter{
                selectedItem ->
            findNavController().navigate(R.id.action_homeFragment_to_shiftListFragment)
        }
        recyclerView.adapter = mAdapter

        viewModelShift.firebaseData.observe(
            viewLifecycleOwner){
                data ->
            mAdapter.updateData(data)
        }

        handler = Handler()
        startAutoScroll()



        viewModelShift.fetchFirebaseData()


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
                        .apply(RequestOptions().dontAnimate())
                        .into(binding.profileImage)

                    name = profileData.name

                    binding.hello.text = "Jambo ${profileData.name}"

                }
            })
        }

        binding.hello.setOnClickListener {

        }

        binding.profileImage.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_activeLocumsFragment)
        }

        binding.notifications.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_notificationsFragment)
        }

        retrieveFCMToken()

    }

    fun retrieveFCMToken(){
        // Get the current user's UID
        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid
        // Retrieve the FCM token
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val fcmToken = task.result

                // Store the FCM token in Firestore under the user's document
                if (currentUserUid != null) {
                    val firestore = FirebaseFirestore.getInstance()
                    val userDocument = firestore.collection("doctor_users").document(currentUserUid)

                    userDocument.update("fcmToken", fcmToken)
                        .addOnSuccessListener {
                            // Token updated successfully
                        }
                        .addOnFailureListener {
                            // Handle token update failure
                        }
                }
            } else {
                // Handle token retrieval failure
            }
        }
    }

    override fun onStart() {
        super.onStart()
        retrieveFCMToken()
    }

    override fun onResume() {
        super.onResume()
        retrieveFCMToken()
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }





    override fun onStop() {
        super.onStop()
        _binding = null
    }


    private fun startAutoScroll() {
        handler.postDelayed({
            if (currentPosition < mAdapter.itemCount - 1) {
                currentPosition++
            } else {
                currentPosition = 0
            }

            recyclerView.smoothScrollToPosition(currentPosition)
            startAutoScroll()
        }, scrollDelay)
    }

    override fun onDestroyView() {
        handler.removeCallbacksAndMessages(null)
        super.onDestroyView()
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacksAndMessages(null)
        _binding = null
    }


//    private fun setupRecyclerView() = binding.shiftLocumsList.apply {
//        hospitalArrayList = ArrayList()
//        getHospitalData()
//        mAdapter = HospitalsAdapter(requireActivity(), hospitalArrayList!!)
//        adapter = mAdapter
//        layoutManager = LinearLayoutManager(requireContext())
//    }

    private fun getHospitalData()  {
        CoroutineScope(Dispatchers.IO).launch {
            val data : ArrayList<Hospital> = ArrayList()

            try {
                mDatabase1 = FirebaseDatabase.getInstance().getReference("hospitals").get().await()
                for (hospitalSnapshot in mDatabase1.children) {
                    val hospital = hospitalSnapshot.getValue(Hospital::class.java)
                    hospitalArrayList?.add(hospital!!)
                    data.add(hospital!!)
                }




//                withContext(Dispatchers.Main) {
//                    mAdapter = HospitalsAdapter(hospitalArrayList!!)
//                    recyclerView.adapter = testAdapter
//
//                    //     Toast.makeText(requireContext(), "Oya!!", Toast.LENGTH_SHORT).show()
//                    //  navigateToShiftDetails()
//                }

            } catch (e: Exception) {

                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
                }
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




//    private fun navigateToShiftDetails() {
//        mAdapter.setOnItemClickListener(object : HospitalsAdapter.onItemClickListener{
//            override fun onItemClick(position: Int) {
//                val hospital = hospitalArrayList?.get(position)
////                val token = ?.token
////                sharedViewModelToken.saveContent(token)
////                if (doctor != null) {
////                    sharedViewModel2.saveContent(doctor)
////                }
//                findNavController().navigate(R.id.action_homeFragment_to_shiftListFragment)
//            }
//        })
//    }
}


