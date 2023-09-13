package com.locums.locumscout.data

data class ActiveLocum(
    val applicant_name: String?,
    val hospital_name: String?,
    val end_date: Long?
){
    constructor() : this(null, null,null)
}