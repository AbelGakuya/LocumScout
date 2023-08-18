package com.locums.locumscout.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.locums.locumscout.data.Abel
import com.locums.locumscout.data.Hospital
import com.locums.locumscout.databinding.AbelBinding
import com.locums.locumscout.databinding.ShiftLocumsBinding

class TestAdapter(val data: ArrayList<Abel>): RecyclerView.Adapter<TestAdapter.TestViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)

        //val view = layoutInflater.inflate(R.layout.topics, parent, false) as CardView
        val binding = ShiftLocumsBinding.inflate(layoutInflater,parent,false)

        return TestViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: TestViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    inner class TestViewHolder(val binding: ShiftLocumsBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: Abel){
            binding.hospitalName.text = item.firstName
            binding.hospitalName2.text = item.lastName


        }
    }
}