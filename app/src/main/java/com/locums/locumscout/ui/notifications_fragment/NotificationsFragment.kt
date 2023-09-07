package com.locums.locumscout.ui.notifications_fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.locums.locumscout.R
import com.locums.locumscout.adapters.NotificationAdapter
import com.locums.locumscout.data.IncomingNotificationData
import com.locums.locumscout.databinding.FragmentNotificationsBinding
import com.locums.locumscout.viewModels.SharedNotificationsViewModel
import com.locums.locumscout.viewModels.SharedViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class NotificationsFragment : Fragment() {

    private lateinit var binding: FragmentNotificationsBinding

    private lateinit var recyclerView: RecyclerView
    val sharedViewModel2: SharedNotificationsViewModel by activityViewModels()
   // private lateinit var notificationsViewModel: NotificationsViewModel

   // private val notifications: MutableList<IncomingNotificationData> = mutableListOf()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNotificationsBinding.inflate(inflater,container,false)
        val view = binding.root

        val applicantId = FirebaseAuth.getInstance().currentUser?.uid
        recyclerView = binding.notificationsList

        val adapter = NotificationAdapter{
            selectedItem ->

            sharedViewModel2.saveContent(selectedItem)
            findNavController().navigate(R.id.action_notificationsFragment_to_locumFragment)
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val notificationsCollection = FirebaseFirestore.getInstance().collection("doctor_users")
            .document(applicantId!!).collection("notifications")


        //QUERY notifications
        notificationsCollection.get()
            .addOnSuccessListener {
                querySnapshot ->
                val notificationsList = ArrayList<IncomingNotificationData>()

                for (document in querySnapshot.documents){
                    val notification = document.toObject(IncomingNotificationData::class.java)
                    if (notification != null){
                        notificationsList.add(notification)
                    }
                }
                adapter.updateNotifications(notificationsList)
            }

        return view
    }


}