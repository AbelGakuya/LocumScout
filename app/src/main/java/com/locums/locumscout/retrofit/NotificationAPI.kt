package com.locums.locumscout.retrofit

import com.locums.locumscout.data.NotificationRequest
import com.locums.locumscout.data.NotificationResponse
import com.locums.locumscout.data.PushNotification
import com.locums.locumscout.other.Constantss.Companion.CONTENT_TYPE
import com.locums.locumscout.other.Constantss.Companion.SERVER_KEY
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotificationAPI {

    @Headers("Authorization: key = ${SERVER_KEY}","Content-Type:$CONTENT_TYPE")
    @POST("fcm/send")
    suspend fun postNotification(
        @Body notification: PushNotification
    ): Response<ResponseBody>
}