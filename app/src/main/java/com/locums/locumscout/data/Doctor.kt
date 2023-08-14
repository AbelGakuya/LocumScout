package com.locums.locumscout.data

data class Doctor(
    var firstName: String?= null,
    var lastName: String?= null,
    var name: String? = firstName + "" + lastName,
    var uid: String? = null,
    var imageUrl: String? = null
)