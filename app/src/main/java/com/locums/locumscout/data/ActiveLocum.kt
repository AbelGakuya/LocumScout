package com.locums.locumscout.data

import java.util.Date

data class ActiveLocum(
    val applicant_name: String?,
    val hospital_name: String?,
    val end_date: Date?
){
    constructor() : this(null, null,null)
}