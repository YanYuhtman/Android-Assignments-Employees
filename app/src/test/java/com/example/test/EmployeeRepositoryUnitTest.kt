package com.example.test

import com.example.test.Model.Employee
import com.example.test.Model.EmployeeRepository
import com.example.test.Model.EmployeeTransport
import com.google.gson.Gson
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class EmployeeRepositoryUnitTest {

    @Test
    fun fetchEmployees(){
        runBlocking {
            val result = EmployeeRepository.fetchEmployees()
            result.getOrNull()?.let {
                print(EmployeeTransport.toJson(EmployeeTransport(it)))
            }
            assertTrue(result.isSuccess)
        }
    }
    @Test
    fun fetchEmployeesFailMaleFormed() = runTest {
        launch {
            val result = EmployeeRepository.fetchEmployees("https://s3.amazonaws.com/sq-mobile-interview/employees_malformed.json")
            result.getOrNull()?.let {
                print(EmployeeTransport.toJson(EmployeeTransport(it)))
            }
            assertTrue(result.isSuccess)
        }
    }
    @Test
    fun fetchEmployeesFailEmpty() = runTest {
        launch {
            val result = EmployeeRepository.fetchEmployees("https://s3.amazonaws.com/sq-mobile-interview/employees_empty.json")
            result.getOrNull()?.let {
                print(EmployeeTransport.toJson(EmployeeTransport(it)))
            }
            assertTrue(result.isSuccess)
        }
    }
    @Test
    fun fetchEmployeePhotoThumb() = fetchEmployeePhoto()
    @Test
    fun fetchEmployeePhoto() = fetchEmployeePhoto(false)

    fun fetchEmployeePhoto(small: Boolean = true) = runTest {

        launch {
            var employee: Employee = Gson().fromJson("""
                    {
                      "uuid": "0d8fcc12-4d0c-425c-8355-390b312b909c",
                      "full_name": "Justine Mason",
                      "phone_number": "5553280123",
                      "email_address": "jmason.demo@squareup.com",
                      "biography": "Engineer on the Point of Sale team.",
                      "photo_url_small": "https://s3.amazonaws.com/sq-mobile-interview/photos/16c00560-6dd3-4af4-97a6-d4754e7f2394/small.jpg",
                      "photo_url_large": "https://s3.amazonaws.com/sq-mobile-interview/photos/16c00560-6dd3-4af4-97a6-d4754e7f2394/large.jpg",
                      "team": "Point of Sale",
                      "employee_type": "FULL_TIME"
                    }
                    """, Employee::class.java)

            val result = EmployeeRepository.fetchPhotoRaw(employee,small)
            result.getOrNull()?.let {
                print("Photo size ${it.size} bytes\n")
            }
            assertTrue(result.isSuccess)

        }
    }
}