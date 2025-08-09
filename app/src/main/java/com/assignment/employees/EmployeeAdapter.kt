package com.assignment.employees

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.assignment.employees.Model.Employee
import com.assignment.employees.databinding.EmployeeExtendedViewHolderBinding
import com.assignment.employees.databinding.EmployeeViewHolderBinding

const val LOG_TAG_ADAPTER_E = "[EmployeesAdapter]"
abstract class AbstractEmployeeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){


    open fun bind(adapter: EmployeeAdapter,employee: Employee){
        fun updateViewState(){
            employee.isExpanded = !employee.isExpanded
            adapter.notifyItemChanged(adapterPosition)
        }
        itemView.setOnLongClickListener {
            Log.e(LOG_TAG_ADAPTER_E,"Item $itemView longClicked")
            updateViewState()
            return@setOnLongClickListener true
        }
        itemView.setOnClickListener {
            Log.e(LOG_TAG_ADAPTER_E,"Item $itemView clicked")
            updateViewState()
        }
    }

}
class EmployeeViewHolder(itemView: View) : AbstractEmployeeViewHolder(itemView) {
    val binding: EmployeeViewHolderBinding = EmployeeViewHolderBinding.bind(itemView)

    override fun bind(adapter: EmployeeAdapter,employee: Employee) { // Assuming you have an Employee data class
        super.bind(adapter,employee)
        binding.textViewEmployeeName.text = employee.fullName
        binding.textViewEmployeeTeam.text = employee.team
        binding.textViewEmployeeEmail.text = employee.emailAddress

        employee.photoUrlSmall?.let { url ->
            Glide.with(itemView.context)
                .load(url)
                .circleCrop()
                .into(binding.imageViewEmployeePhoto)
        }
    }

}

class ExtendedEmployeeViewHolder(itemView: View) : AbstractEmployeeViewHolder(itemView) {

    val binding = EmployeeExtendedViewHolderBinding.bind(itemView)
    override fun bind(adapter: EmployeeAdapter,employee: Employee) {
        super.bind(adapter,employee)
        binding.textViewEmployeeNameExtended.text = employee.fullName
        binding.textViewEmployeeTeamExtended.text = employee.team
        binding.textViewEmployeeEmailExtended.text = employee.emailAddress

        var thumbRequest: RequestBuilder<Drawable>? = null
        employee.photoUrlSmall?.let {
            thumbRequest = Glide.with(itemView.context).load(it)
                .circleCrop()
        }
        // Photo
        employee.photoUrlLarge?.let { url -> // Or photoUrlLarge for extended view
            Glide.with(itemView.context).load(url)
                .thumbnail(thumbRequest)
                .circleCrop()
                .into(binding.imageViewEmployeePhotoExtended)

        }

        // Phone
        if (employee.phoneNumber.isNullOrEmpty()) {
            binding.linearLayoutPhone.visibility = View.GONE
        } else {
            binding.linearLayoutPhone.visibility = View.VISIBLE
            binding.textViewEmployeePhoneExtended.text = employee.phoneNumber
        }

        // Employee Type (Assuming EmployeeType is an enum with a user-friendly string representation)
        binding.textViewEmployeeTypeExtended.text = employee.employeeType.toString() // Or a more formatted string

        // Biography
        if (employee.biography.isNullOrEmpty()) {
            binding.textViewEmployeeBiographyExtended.visibility = View.GONE
        } else {
            binding.textViewEmployeeBiographyExtended.visibility = View.VISIBLE
            binding.textViewEmployeeBiographyExtended.text = employee.biography
        }

    }
}

enum class EmployeeViewType{
    REGULAR,
    EXPANDED
}
data class EmployeeAdapter(val employees: List<Employee>) : RecyclerView.Adapter<AbstractEmployeeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractEmployeeViewHolder {
        when(viewType) {
            EmployeeViewType.REGULAR.ordinal ->
                return EmployeeViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.employee_view_holder, parent, false)


                )
            EmployeeViewType.EXPANDED.ordinal ->
                return ExtendedEmployeeViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.employee_extended_view_holder, parent, false)
                )
            else -> throw IllegalArgumentException("Invalid view type identifier $viewType")
        }
    }

    override fun onBindViewHolder(holder: AbstractEmployeeViewHolder,position: Int) {
        holder.bind(this,employees[position])
    }

    override fun getItemCount(): Int = employees.size

    override fun getItemViewType(position: Int): Int {
        return if (employees[position].isExpanded) EmployeeViewType.EXPANDED.ordinal else EmployeeViewType.REGULAR.ordinal
    }

}