package com.locums.locumscout.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.locums.locumscout.data.Hospital
//import com.locums.locumscout.databinding.LocumsListBinding
import com.locums.locumscout.databinding.ShiftLocumsBinding

class HospitalsAdapter(var c: Context, var hospitalsList: ArrayList<Hospital>)
    :RecyclerView.Adapter<HospitalsAdapter.HospitalsViewHolder>() {

    private lateinit var mListener: onItemClickListener

    // lateinit var binding: DoctorsListBinding


    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HospitalsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        //val view = layoutInflater.inflate(R.layout.topics, parent, false) as CardView
        val binding = ShiftLocumsBinding.inflate(layoutInflater,parent,false)

        return HospitalsViewHolder(binding, mListener)
    }

    override fun getItemCount(): Int {
        return hospitalsList.size
    }

    override fun onBindViewHolder(holder: HospitalsViewHolder, position: Int) {
        val item = hospitalsList[position]
        holder.binding.isHospital = item
    }

    class HospitalsViewHolder(val binding: ShiftLocumsBinding, listener: onItemClickListener)
        : RecyclerView.ViewHolder(binding.root){
        init {

            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }


    }
}