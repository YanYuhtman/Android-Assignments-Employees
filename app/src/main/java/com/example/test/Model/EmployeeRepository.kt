package com.example.test.Model

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class EmployeeRepository {
    companion object {
        private val client = OkHttpClient()
        private val employeesUrl = "https://s3.amazonaws.com/sq-mobile-interview/employees.json"

        suspend fun fetchEmployees(url: String = employeesUrl): Result<List<Employee>> {
            return withContext(Dispatchers.IO) {
                try {
                    val request = Request.Builder()
                        .url(url)
                        .build()

                    client.newCall(request).execute().use { response ->
                        if (!response.isSuccessful) {
                            return@withContext Result.failure(IOException("Unexpected code $response"))
                        }
                        val responseBody = response.body?.string()
                        if (responseBody != null) {
                            val employeeResponse = EmployeeTransport.fromJson(responseBody)
                            Result.success(employeeResponse.employees)
                        } else {
                            Result.failure(IOException("Empty response body"))
                        }
                    }
                } catch (e: IOException) {
                    Result.failure(e)
                } catch (e: Exception) {
                    Result.failure(e)
                }
            }
        }

        suspend fun fetchPhotoRaw(employee: Employee, thumb: Boolean = true): Result<ByteArray> {
            return withContext(Dispatchers.IO) {
                try {
                    var request = when (thumb) {
                        true -> {
                            employee.photoUrlSmall?.let {
                                Request.Builder()
                                    .url(employee.photoUrlSmall)
                                    .build()
                            }
                                ?: return@withContext Result.failure(IOException("Employee has no photo thumbnail"))

                        }

                        false -> {
                            employee.photoUrlLarge?.let {
                                Request.Builder()
                                    .url(employee.photoUrlLarge)
                                    .build()
                            }
                                ?: return@withContext Result.failure(IOException("Employee has no photo"))
                        }
                    }
                    client.newCall(request).execute().use { response ->
                        if (!response.isSuccessful) {
                            return@withContext Result.failure(IOException("Unexpected code $response"))
                        }
                        val responseData = response.body?.bytes()
                        if (responseData != null) {
                            Result.success(responseData)
                        } else {
                            Result.failure(IOException("Empty response body"))
                        }
                    }

                } catch (e: IOException) {
                    Result.failure(e)
                } catch (e: Exception) {
                    Result.failure(e)
                }
            }
        }
    }
}