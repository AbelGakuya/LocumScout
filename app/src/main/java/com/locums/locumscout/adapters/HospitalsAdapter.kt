package com.locums.locumscout.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.locums.locumscout.data.Hospital
//import com.locums.locumscout.databinding.LocumsListBinding
import com.locums.locumscout.databinding.ShiftLocumsBinding

class HospitalsAdapter(
                       private val onItemClick:(Hospital) -> Unit)
    :RecyclerView.Adapter<HospitalsAdapter.HospitalsViewHolder>() {

    private val data = mutableListOf<Hospital>()

    fun updateData(newData: List<Hospital>){
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HospitalsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        //val view = layoutInflater.inflate(R.layout.topics, parent, false) as CardView
        val binding = ShiftLocumsBinding.inflate(layoutInflater,parent,false)

        return HospitalsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: HospitalsViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    inner class HospitalsViewHolder(val binding: ShiftLocumsBinding)
        : RecyclerView.ViewHolder(binding.root){
       fun bind(item: Hospital){
           binding.hospitalName.text = item.hospitalName
           Glide.with(binding.hospitalImage.context)
               .load(item.imageUrl)
               .into(binding.hospitalImage)

           binding.root.setOnClickListener { onItemClick(item) }
       }


    }
}