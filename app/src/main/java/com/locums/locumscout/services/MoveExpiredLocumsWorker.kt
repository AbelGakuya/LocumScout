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


            val db = FirebaseFirestore.getInstance()
            val currentDate = Date()
            val uid = FirebaseAuth.getInstance().currentUser?.uid
            val locumCollection = db.collection("doctor_users").document(uid!!)
                .collection("ActiveLocums")

            locumCollection.whereLessThanOrEqualTo("end_date", currentDate)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        db.collection("doctor_users").document(uid!!)
                            .collection("completedLocum").add(document.data)

                        locumCollection.document(document.id).delete()

                    }
                }
                .addOnFailureListener { }

            Log.d("FirestoreWorker", "Worker is running")
            return Result.success()

        } catch (e: Exception){
            Log.e("FirestoreWorker", "Worker failed: ${e.message}")
            return Result.failure()
        }
    }
}