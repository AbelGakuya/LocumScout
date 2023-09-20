package com.locums.locumscout

import android.app.Application
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.firebase.FirebaseApp
//import com.locums.locumscout.services.LocumBackgroundService
import com.locums.locumscout.services.MoveExpiredLocumsWorker
import java.util.concurrent.TimeUnit

class BaseApplication: Application() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)


//        val serviceIntent = Intent(this, LocumBackgroundService::class.java)
//        startForegroundService(serviceIntent)

        scheduleWorker()

    }

    private fun scheduleWorker() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val repeatInterval = 2L
        val repeatIntervalMillis = TimeUnit.MINUTES.toMillis(repeatInterval)

        val periodicWorkRequest = PeriodicWorkRequest.Builder(
            MoveExpiredLocumsWorker::class.java,
            repeatIntervalMillis,
            TimeUnit.MILLISECONDS
        )
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            "FirestoreQueryWork",
            ExistingPeriodicWorkPolicy.REPLACE,
            periodicWorkRequest
        )
    }
}