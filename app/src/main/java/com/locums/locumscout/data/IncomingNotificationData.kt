package com.locums.locumscout.data

data class IncomingNotificationData(
    val title: String?,
    val message: String?,
    val timeStamp: Long,
    val applicantId: String?
) {
    constructor() : this(null, null, 0L, null)
}
