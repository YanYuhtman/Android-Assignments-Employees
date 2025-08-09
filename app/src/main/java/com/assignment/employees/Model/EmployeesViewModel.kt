package com.assignment.employees.Model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

enum class EmployeeLoadingState{

    INIT,
    LOADING,
    RELOADING,
    LOADED,
    ERROR
}

class EmployeesViewModel: ViewModel() {

    var employees: List<Employee> = emptyList()
    var loadingState: MutableLiveData<EmployeeLoadingState> = MutableLiveData(EmployeeLoadingState.INIT)
    init {
        viewModelScope.launch {
            loadingState.postValue(EmployeeLoadingState.LOADING)
            var result = EmployeeRepository.fetchEmployees()
            result.onSuccess {
                employees =result.getOrDefault(emptyList<Employee>())
                loadingState.postValue(EmployeeLoadingState.LOADED)
            }
            result.onFailure {
                loadingState.postValue(EmployeeLoadingState.ERROR)
                Log.e("Unable to fetch employees", it.message ?: "")
            }
        }
    }
    fun reloadEmployees() = loadEmployees(true)
    private fun loadEmployees(reload: Boolean = false) {
        viewModelScope.launch {
            loadingState.postValue(if (reload) EmployeeLoadingState.RELOADING else EmployeeLoadingState.LOADING)
            var result = EmployeeRepository.fetchEmployees()
            result.onSuccess {
                employees =result.getOrDefault(emptyList<Employee>())
                loadingState.postValue(EmployeeLoadingState.LOADED)
            }
            result.onFailure {
                loadingState.postValue(EmployeeLoadingState.ERROR)
                Log.e("Unable to fetch employees", it.message ?: "")
            }
        }
    }
}