package com.assignment.employees

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.assignment.employees.Model.EmployeeLoadingState
import com.assignment.employees.Model.EmployeesViewModel
import com.assignment.employees.databinding.EmployeesActivityBinding

const val LOG_TAG_ACTIVITY_E = "EmployeesActivity"
class EmployeesActivity : AppCompatActivity() {

    lateinit var binding: EmployeesActivityBinding
    private val viewModel: EmployeesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = EmployeesActivityBinding.inflate(layoutInflater)

        setContentView(binding.root)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        binding.button.setOnClickListener  {
            viewModel.reloadEmployees()
        }

        viewModel.loadingState.observe(this){
            when(it){
                EmployeeLoadingState.LOADING -> {
                    Log.i(LOG_TAG_ACTIVITY_E,"Loading employees")
                }
                EmployeeLoadingState.RELOADING -> {
                    Log.i(LOG_TAG_ACTIVITY_E,"Reloading employees")
                }
                EmployeeLoadingState.LOADED -> {
                    Log.i("[Test]","Successfully loaded employees")
                    binding.recyclerView.adapter = EmployeeAdapter(viewModel.employees)
                }
                EmployeeLoadingState.ERROR -> {
                    Log.e(LOG_TAG_ACTIVITY_E,"Unable to load employees!")
                }
                else -> {}
            }

        }





//        //TODO: remove this line
//        Glide.get(this.applicationContext).clearMemory()

    }
}