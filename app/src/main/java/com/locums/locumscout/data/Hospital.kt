package com.locums.locumscout.data

import java.util.Date


data class Hospital(
    val hospitalName: String?,
    val title: String?,
    val imageUrl: String?,
    val uid: String?,
    val vacancyId: String?,
    val endDate: Date?,
    val startDate: Date?
)
