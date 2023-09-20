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
import com.locums.locumscout.data.FirestoreNotification
import com.locums.locumscout.data.IncomingNotificationData
import com.locums.locumscout.databinding.FragmentNotificationsBinding
import com.locums.locumscout.viewModels.SharedNotificationsViewModel
//import com.github.marlonlom.timeago.TimeAgo
import com.locums.locumscout.viewModels.SharedViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit


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
            findNavController().navigate(R.id.action_notificationsFragment_to_notificationsDetailsFragment)
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val notificationsCollection = FirebaseFirestore.getInstance().collection("doctor_users")
            .document(applicantId!!).collection("notifications")


        //QUERY notifications
        notificationsCollection.orderBy("timeStamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener {
                querySnapshot ->
                val notificationsList = ArrayList<IncomingNotificationData>()
                for (document in querySnapshot.documents){
                    val notification = document.toObject(IncomingNotificationData::class.java)
//                    val title = document.getString("title")
//                    val message = document.getString("message")
//                    val timestamp = document.getTimestamp("timeStamp")
//                    val notificationId = document.id
//                    val notification = FirestoreNotification(title, message, timestamp,notificationId)

                    if (notification != null){
//                        val newNotification = IncomingNotificationData(notification?.title,
//                            notification?.message,
//                            notification?.timeStamp,
//                            notification?.applicantId
//                            )

                        val timestamp = notification?.timeStamp?.toDate()?.time ?: 0L
                        //val timestampP = timestamp?.toDate()?.time ?: 0L
                        val currentTime = System.currentTimeMillis()

                        val timeDifference = currentTime - timestamp

                        val seconds = TimeUnit.MILLISECONDS.toSeconds(timeDifference)
                        val minutes = TimeUnit.MILLISECONDS.toMinutes(timeDifference)
                        val hours = TimeUnit.MILLISECONDS.toHours(timeDifference)
                        val days = TimeUnit.MILLISECONDS.toDays(timeDifference)
                        val timeAgo = when{
                            seconds <60 -> "Just now"
                            minutes == 1L -> "1 minute ago"
                            minutes < 60 -> "$minutes minutes ago"
                            hours == 1L -> "1 hour ago"
                            hours < 24 -> "$hours hours ago"
                            days == 1L -> "1 day ago"
                            else -> "$days days ago"
                        }
                        notification.timeAgo = timeAgo

                        notificationsList.add(notification)
                    }
                }
                adapter.updateNotifications(notificationsList)
            }

        return view
    }


}