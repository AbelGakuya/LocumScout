package com.locums.locumscout.services

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.locums.locumscout.data.ActiveLocum

class LocumBackgroundService : Service() {
    private val handler = Handler()
    private val delay = 60 * 1000L

    private val db = FirebaseFirestore.getInstance()

    private val uid = FirebaseAuth.getInstance().currentUser?.uid

    private val locumCollection = db.collection("doctor_users").document(uid!!)
        .collection("ActiveLocums")

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        handler.postDelayed(locumStatusUpdater, delay)

        return START_STICKY
    }

    override fun onDestroy() {
        handler.removeCallbacks(locumStatusUpdater)
        super.onDestroy()
    }

    private val locumStatusUpdater = object : Runnable {
        override fun run() {
            locumCollection.get().addOnSuccessListener {
                querySnapshot ->
                val hasActiveLocums = !querySnapshot.isEmpty
                if (!hasActiveLocums){
                    stopSelf()
                } else {
                    checkAndUpdateLocumStatuses()
                    handler.postDelayed(this,delay)

                }
            }


        }
    }

    private fun checkAndUpdateLocumStatuses(){
        locumCollection.get()
            .addOnSuccessListener {
                querySnapshot ->
                for (document in querySnapshot.documents){
                    val locum = document.toObject(ActiveLocum::class.java)
                    if (locum != null){
                        if (locum.end_date!! <= System.currentTimeMillis()){
                            val completedCollection = db
                                .collection("doctor_users")
                                .document(uid!!)
                                .collection("completedLocums")

                            completedCollection.document(document.id).set(locum)

                            locumCollection.document(document.id).delete()
                        }
                    }
                }

            }
            .addOnFailureListener {  }
    }



    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

}