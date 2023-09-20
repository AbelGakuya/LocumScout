package com.locums.locumscout.ui.user_profile_fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.locums.locumscout.R
import com.locums.locumscout.databinding.FragmentUserProfileBinding
import com.locums.locumscout.other.Constants
import com.locums.locumscout.viewModels.FirebaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class UserProfileFragment : Fragment() {
    private var _binding : FragmentUserProfileBinding? = null

    private val binding get() = _binding!!

    lateinit var auth: FirebaseAuth
    private lateinit var viewModel: FirebaseViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
                    Glide.with(this@UserProfileFragment)
                        .load(profileData.imageUrl)
                        .error(R.drawable.baseline_lock_24)
                        .placeholder(R.drawable.baseline_person_24)
                        .apply(RequestOptions().dontAnimate())
                        .into(binding.profileImage)

                    Constants.name = profileData.name

                    binding.userName.text = "${profileData.name}"

                }
            })
        }
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


}