package com.assignment.employees.Model

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName

data class EmployeeTransport (
    @SerializedName("employees")
    val employees: List<Employee>)
{

    companion object {

        fun fromJson(json: String): EmployeeTransport = Gson().fromJson(json, EmployeeTransport::class.java)

        fun toJson(employees: EmployeeTransport): String = GsonBuilder()
            .setPrettyPrinting()
            .create().toJson(employees)
    }
}
