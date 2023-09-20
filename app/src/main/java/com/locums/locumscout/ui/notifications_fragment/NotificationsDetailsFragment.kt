package com.locums.locumscout.ui.notifications_fragment

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.locums.locumscout.R
import com.locums.locumscout.databinding.FragmentNotificationsDetailsBinding
import com.locums.locumscout.other.Constants.endDate
import com.locums.locumscout.viewModels.SharedNotificationsViewModel

class NotificationsDetailsFragment : Fragment() {

    private lateinit var binding: FragmentNotificationsDetailsBinding
    val sharedViewModel2: SharedNotificationsViewModel by activityViewModels()

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel2.content.observe(viewLifecycleOwner, {
                content ->
            val notificationId = content.notificationId
            markNotificationAsRead(notificationId)

        })
    }

    private fun markNotificationAsRead(notificationId: String?) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val db = FirebaseFirestore.getInstance()
        val notificationDocRef = db.collection("doctor_users")
            .document(userId!!)
            .collection("notifications")

        val query = notificationDocRef.whereEqualTo("notificationId", notificationId)

        query.get()
            .addOnSuccessListener {
                querySnapshot ->
                for (document in querySnapshot){
                    val notificationId = document.id
                    val notificationRef = notificationDocRef.document(notificationId)
                    val updates = hashMapOf(
                            "read" to true
                            )

                    notificationRef.update(updates as Map<String, Any>)
                        .addOnSuccessListener {
                            Log.e("markedRead", "markedRead successfully")
                        }
                        .addOnFailureListener {
                                e ->
                            Log.e("markedRead", "error markedRead ${e.message}")
                        }
                }
            }






    }



}