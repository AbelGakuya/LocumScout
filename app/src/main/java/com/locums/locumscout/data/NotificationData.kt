package com.locums.locumscout.data

data class NotificationData(
    val title: String,
    val message: String,
    val applicantId: String?,
    val hospitalId: String,
    val endDate: String?
)