package com.locums.locumscout.repo

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.locums.locumscout.data.ActiveLocum
import com.locums.locumscout.data.CompletedLocum
import com.locums.locumscout.data.Hospital
import kotlinx.coroutines.tasks.await

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
                val endDate = hospitalSnapshot.getTimestamp("endDate")?.toDate()


                data.add(Hospital(hospitalName,title,imageUrl,uid,endDate))
            }
        } catch (e: Exception){
//            withContext(Dispatchers.Main) {
//                Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
            }
        return data
        }

    suspend fun getActiveLocums(uid: String?): List<ActiveLocum>{
        val data = mutableListOf<ActiveLocum>()

        try {
            val snapshot = firestore
                .collection("doctor_users")
                .document(uid!!)
                .collection("ActiveLocums")
              //  .orderBy("timestamp", Query.Direction.DESCENDING)
                .get().await()
            for (locumSnapshot in snapshot.documents){
                val applicantName = locumSnapshot.getString("applicant_name")
                val hospitalName = locumSnapshot.getString("hospital_name")
                val endDate = locumSnapshot.getDate("end_date")

                data.add(ActiveLocum(applicantName,hospitalName,endDate))
            }
        } catch (e: Exception){
//            withContext(Dispatchers.Main) {
//                Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
        }
        return data
    }

    suspend fun getCompletedLocums(uid: String?): List<CompletedLocum>{
        val data = mutableListOf<CompletedLocum>()

        try {
            val snapshot = firestore
                .collection("doctor_users")
                .document(uid!!)
                .collection("completedLocum")
               // .orderBy("timestamp", Query.Direction.DESCENDING)
                .get().await()
            for (locumSnapshot in snapshot.documents){
                val applicantName = locumSnapshot.getString("applicant_name")
                val hospitalName = locumSnapshot.getString("hospital_name")
                val endDate = locumSnapshot.getDate("end_date")

                data.add(CompletedLocum(applicantName,hospitalName,endDate))
            }
        } catch (e: Exception){
//            withContext(Dispatchers.Main) {
//                Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
        }
        return data
    }




    }
