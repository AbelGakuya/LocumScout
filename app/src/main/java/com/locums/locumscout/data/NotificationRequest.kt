package com.locums.locumscout.data
data class NotificationRequest(
    val title: String,
    val body: String,
    val tokens: List<String>
)