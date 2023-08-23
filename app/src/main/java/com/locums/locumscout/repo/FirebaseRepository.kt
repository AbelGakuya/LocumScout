package com.locums.locumscout.repo

import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.locums.locumscout.data.Hospital
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FirebaseRepository {
    private val firestore = FirebaseFirestore.getInstance()

    //Fetch Firebase Data
    suspend fun getFirebaseData(): List<Hospital>{
        val data = mutableListOf<Hospital>()

        try {
            val snapshot = firestore.collection("vacancies").get().await()
            for (hospitalSnapshot in snapshot.documents){
                val hospitalName = hospitalSnapshot.getString("hospitalName")
                val imageUrl = hospitalSnapshot.getString("imageUrl")
                val uid = hospitalSnapshot.getString("hospitalId")
                val title = hospitalSnapshot.getString("jobTitle")


                data.add(Hospital(hospitalName,title,imageUrl,uid))
            }
        } catch (e: Exception){
//            withContext(Dispatchers.Main) {
//                Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
            }
        return data
        }
    }
