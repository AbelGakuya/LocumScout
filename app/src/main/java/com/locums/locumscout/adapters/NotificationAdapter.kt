package com.locums.locumscout.adapters

import com.locums.locumscout.R
import com.locums.locumscout.data.IncomingNotificationData
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.locums.locumscout.data.Hospital
import com.locums.locumscout.databinding.NotificationItemBinding

//import com.locums.locumscouth.R
//import com.locums.locumscouth.model.IncomingNotificationData

class NotificationAdapter(
    private val onItemClick:(IncomingNotificationData) -> Unit) :
    RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>(){


    private val notificationsList: MutableList<IncomingNotificationData> = mutableListOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotificationAdapter.NotificationViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = NotificationItemBinding.inflate(layoutInflater,parent,false)

        return NotificationViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: NotificationAdapter.NotificationViewHolder,
        position: Int
    ) {
        val notification = notificationsList[position]
       holder.bind(notification)
    }

    override fun getItemCount(): Int {
        return notificationsList.size
    }

    fun updateNotifications(notifications: List<IncomingNotificationData>){
        notificationsList.clear()
        notificationsList.addAll(notifications)
        notifyDataSetChanged()
    }

    inner class NotificationViewHolder(val binding: NotificationItemBinding):
        RecyclerView.ViewHolder(binding.root){

        fun bind(item: IncomingNotificationData){
            binding.messageTextView.text = item.message
            binding.titleTextView.text = item.title
            binding.timeTextView.text = item.timeStamp.toString()

            binding.root.setOnClickListener { onItemClick(item) }
        }




    }

}