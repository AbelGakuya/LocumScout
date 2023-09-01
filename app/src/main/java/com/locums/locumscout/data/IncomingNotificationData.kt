package com.locums.locumscout.data

data class IncomingNotificationData(

    val title: String?,
    val message: String?,
    val timeStamp: Long,
    val hospitalId: String?
)