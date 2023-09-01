package com.locums.locumscout.ui.notifications_fragment

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.locums.locumscout.R
import com.locums.locumscout.databinding.FragmentNotificationsDetailsBinding

class NotificationsDetailsFragment : Fragment() {

    private lateinit var binding: FragmentNotificationsDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNotificationsDetailsBinding.inflate(inflater,container,false)
        val view = binding.root

        val hospitalId = activity?.intent?.getStringExtra("hospitalId")

        val applicantId = FirebaseAuth.getInstance().currentUser?.uid

        val db = FirebaseFirestore.getInstance()

        db.collection("applications")
            .whereEqualTo("Applicant's_uid", applicantId)
            .whereEqualTo("hospitalId", hospitalId)
            .get()
            .addOnSuccessListener {
                    querySnapshot ->
                for (document in querySnapshot.documents){
                    val applicantId = document.getString("Applicants_name")
                    val hospitalId = document.getString("hospitalId")


                    binding.txt.text = hospitalId
//
                    binding.txt1.text = applicantId
                }
            }
            .addOnFailureListener {
                    exception ->
                Log.e("Firestore","Error getting documents: ${exception.message}")
            }


        return view
    }

}