package com.assignment.employees.Model

import com.google.gson.annotations.SerializedName

data class Employee(
    @SerializedName("uuid")
    val uuid: String,

    @SerializedName("full_name")
    val fullName: String,

    @SerializedName("phone_number")
    val phoneNumber: String?, // Nullable as it might be missing

    @SerializedName("email_address")
    val emailAddress: String,

    @SerializedName("biography")
    val biography: String?, // Nullable as it might be missing

    @SerializedName("photo_url_small")
    val photoUrlSmall: String?, // Nullable as it might be missing

    @SerializedName("photo_url_large")
    val photoUrlLarge: String?, // Nullable as it might be missing

    @SerializedName("team")
    val team: String,

    @SerializedName("employee_type")
    val employeeType: EmployeeType
){
    var isExpanded = false
}
// Enum for the different types of employees
enum class EmployeeType {
    @SerializedName("FULL_TIME")
    FULL_TIME,

    @SerializedName("PART_TIME")
    PART_TIME,

    @SerializedName("CONTRACTOR")
    CONTRACTOR
}
