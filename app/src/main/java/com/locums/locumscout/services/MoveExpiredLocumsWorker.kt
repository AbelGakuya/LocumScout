package com.locums.locumscout.services

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Date

class MoveExpiredLocumsWorker(context: Context,
    params: WorkerParameters) :
Worker(context,params){

    override fun doWork(): Result {

        try {
            val uid = FirebaseAuth.getInstance().currentUser?.uid
            if (uid == null){
                Log.e("FirestoreWorker", "No user")
                return Result.failure()
            }

            val db = FirebaseFirestore.getInstance()
            Log.d("Firestore", "starting query")
            val currentDate = Date()

            val locumCollection = db.collection("doctor_users").document(uid!!)
                .collection("ActiveLocums")

            locumCollection
                //.whereLessThanOrEqualTo("end_date", currentDate)
                .get()
                .addOnSuccessListener { querySnapshot ->

                    Log.d("Firestore", "successful ${querySnapshot.size()}")
                    for (document in querySnapshot.documents) {
                        val endDate = document.getTimestamp("end_date")?.toDate()
                        if (endDate != null && endDate <= currentDate){
                            db.collection("doctor_users").document(uid!!)
                                .collection("completedLocum").add(document.data!!)
                                .addOnSuccessListener {
                                    Log.e("Firestore", "Transefered to completed")
                                    db.collection("doctor_users").document(uid!!)
                                        .collection("ActiveLocums").document(document.id).delete().addOnSuccessListener {
                                            Log.e("Firestore", "deletted from active")
                                        }
                                }
                                .addOnFailureListener {  }
                        }
                    }
                }
                .addOnFailureListener {
                    exception ->
                    Log.d("Firestorefail", "Error querying ${exception.message}")
                }

            Log.d("FirestoreWorker", "Worker is running")
            return Result.success()

        } catch (e: Exception){
            Log.e("FirestoreWorker", "Worker failed: ${e.message}")
            return Result.failure()
        }
    }
}