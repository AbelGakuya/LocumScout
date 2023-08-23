package com.locums.locumscout.retrofit

import com.locums.locumscout.data.NotificationRequest
import com.locums.locumscout.data.NotificationResponse
import okhttp3.Call
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("send-notification")
    suspend fun sendNotification(@Body request: NotificationRequest): NotificationResponse
}