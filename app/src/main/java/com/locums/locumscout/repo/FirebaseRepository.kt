package com.locums.locumscout.repo

import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.firebase.database.FirebaseDatabase
import com.locums.locumscout.data.Hospital
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FirebaseRepository {
    private val firestore = FirebaseDatabase.getInstance()

    //Fetch Firebase Data
    suspend fun getFirebaseData(): List<Hospital>{
        val data = mutableListOf<Hospital>()

        try {
            val snapshot = firestore.getReference("hospital").get().await()
            for (hospitalSnapshot in snapshot.children){
                val hospital = hospitalSnapshot.getValue(Hospital::class.java)
                data.add(hospital!!)
            }
        } catch (e: Exception){
//            withContext(Dispatchers.Main) {
//                Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
            }
        return data
        }
    }
