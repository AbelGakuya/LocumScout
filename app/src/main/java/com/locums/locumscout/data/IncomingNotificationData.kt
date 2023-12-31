package com.locums.locumscout.data

import com.google.firebase.Timestamp

data class IncomingNotificationData(
    val title: String?,
    val message: String?,
    val timeStamp: Timestamp?,
    val applicantId: String?,
    val notificationId: String?,
    var read: Boolean = false,
    var timeAgo: String? = null

) {
    constructor() : this(null, null, null, null,null,false,null)
}
