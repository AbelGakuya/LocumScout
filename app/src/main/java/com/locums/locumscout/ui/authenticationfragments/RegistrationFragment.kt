package com.locums.locumscout.ui.authenticationfragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.locums.locumscout.R
import com.locums.locumscout.databinding.FragmentRegistrationBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class RegistrationFragment : Fragment() {

    private lateinit var binding: FragmentRegistrationBinding

    lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        binding = FragmentRegistrationBinding.inflate(inflater,container, false)
        val view = binding.root

        auth = FirebaseAuth.getInstance()

        binding.btnRegister.setOnClickListener {

            registerUser()
            //findNavController().navigate(R.id.action_registrationFragment_to_homeFragment)
        }

        return view
    }

    private fun registerUser(){
        val email = binding.edtEmail.text.toString()
        val password = binding.edtPassword.text.toString()
        if (email.isNotEmpty() && password.isNotEmpty()){
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    auth.createUserWithEmailAndPassword(email, password).await()
                    withContext(Dispatchers.Main){
                        checkLoggedInState()
                    }

                } catch (e:Exception){
                    withContext(Dispatchers.Main){
                        Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }


    private fun checkLoggedInState(){
        if (auth.currentUser == null){
            Toast.makeText(requireContext(), "You are not logged in", Toast.LENGTH_SHORT).show()
        } else {
            findNavController().navigate(R.id.action_registrationFragment_to_homeFragment)
        }
    }


}