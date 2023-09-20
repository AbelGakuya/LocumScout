package com.locums.locumscout.data

import com.google.firebase.Timestamp

data class FirestoreNotification(
    val title: String?,
    val message: String?,
    val timeStamp: Timestamp?,

    val notificationId: String?,
    var timeAgo: String? = null
) {
    constructor() : this(null, null, null,null,null)
}

