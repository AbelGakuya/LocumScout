package com.locums.locumscouth.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.locums.locumscout.data.ActiveLocum
import com.locums.locumscout.databinding.ActiveLocumsBinding
//import com.locums.locumscouth.databinding.ActiveLocumsBinding
//import com.locums.locumscouth.databinding.NotificationItemBinding
//import com.locums.locumscouth.model.ActiveLocum
//import com.locums.locumscouth.model.IncomingNotificationData

class ActiveLocumsAdapter(
    private val onItemClick:(ActiveLocum) -> Unit
) :
    RecyclerView.Adapter<ActiveLocumsAdapter.ActiveLocumsViewHolder>() {

    private val activeLocumsList: MutableList<ActiveLocum> = mutableListOf()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ActiveLocumsAdapter.ActiveLocumsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        val binding = ActiveLocumsBinding.inflate(layoutInflater,parent,false)

        return ActiveLocumsViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ActiveLocumsAdapter.ActiveLocumsViewHolder,
        position: Int
    ) {
        val active_locum = activeLocumsList[position]

        holder.bind(active_locum)
    }

    override fun getItemCount(): Int {
        return activeLocumsList.size
    }

    fun updateActiveLocums(activeLocums: List<ActiveLocum>){
        activeLocumsList.clear()
        activeLocumsList.addAll(activeLocums)
        notifyDataSetChanged()
    }

    inner class ActiveLocumsViewHolder(val binding: ActiveLocumsBinding):
        RecyclerView.ViewHolder(binding.root){

        fun bind(item: ActiveLocum){

            binding.applicantName.text = item.applicant_name
            binding.hospitalName.text = item.hospital_name
            binding.startDate.text = item.start_date.toString()
            binding.endDate.text = item.end_date.toString()

            binding.root.setOnClickListener { onItemClick(item) }
        }

    }


}