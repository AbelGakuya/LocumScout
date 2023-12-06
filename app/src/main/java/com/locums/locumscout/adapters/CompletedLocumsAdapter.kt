package com.locums.locumscout.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.locums.locumscout.data.ActiveLocum
import com.locums.locumscout.data.CompletedLocum
import com.locums.locumscout.databinding.ActiveLocumsBinding
import com.locums.locumscout.databinding.CompletedLocumsBinding
import com.locums.locumscouth.adapters.ActiveLocumsAdapter

class CompletedLocumsAdapter(private val onItemClick:(CompletedLocum) -> Unit)
    :
    RecyclerView.Adapter<CompletedLocumsAdapter.CompletedLocumsViewHolder>(){

    private val completedLocumsList: MutableList<CompletedLocum> = mutableListOf()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CompletedLocumsAdapter.CompletedLocumsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        val binding = CompletedLocumsBinding.inflate(layoutInflater,parent,false)

        return CompletedLocumsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return completedLocumsList.size
    }

    override fun onBindViewHolder(
        holder:CompletedLocumsAdapter.CompletedLocumsViewHolder,
        position: Int
    ) {
        val completed_locum = completedLocumsList[position]

        holder.bind(completed_locum)
    }

    fun updateActiveLocums(completedLocums: List<CompletedLocum>){
        completedLocumsList.clear()
        completedLocumsList.addAll(completedLocums)
        notifyDataSetChanged()
    }

    inner class CompletedLocumsViewHolder(val binding: CompletedLocumsBinding):
        RecyclerView.ViewHolder(binding.root){

        fun bind(item: CompletedLocum){

            binding.applicantName.text = item.applicant_name
            binding.hospitalName.text = item.hospital_name
            binding.endDate.text = item.end_date.toString()

            binding.root.setOnClickListener { onItemClick(item) }
        }

    }

}