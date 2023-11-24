package com.locums.locumscout.ui.authenticationfragments

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.locums.locumscout.R
import com.locums.locumscout.databinding.FragmentSplashBinding
import com.locums.locumscout.other.Constants


class SplashFragment : Fragment() {
    private lateinit var binding: FragmentSplashBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSplashBinding.inflate(inflater,container,false)
        val view = binding.root


//        Handler().postDelayed({
//            if (requireActivity().intent.action == Constants.ACTION_SHOW_APPLICANTS_DETAIL_FRAGMENT){
//                findNavController().navigate(R.id.action_splashFragment_to_notificationsDetailsFragment)
//            } else {
//            findNavController().navigate(R.id.action_splashFragment_to_loginFragment)}
//
//        }, 1500)

        checkLoggedInState()
        return view
    }

    private fun checkLoggedInState(){
        if (FirebaseAuth.getInstance().currentUser == null){
            Handler().postDelayed({
                findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
            },1500)
        } else {
            if (requireActivity().intent.action == Constants.ACTION_SHOW_APPLICANTS_DETAIL_FRAGMENT){
                Handler().postDelayed({
                    findNavController().navigate(R.id.action_splashFragment_to_notificationsDetailsFragment)
                },500)
            } else {
                Handler().postDelayed({
                    findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
                },1500)
            }
    }


}}