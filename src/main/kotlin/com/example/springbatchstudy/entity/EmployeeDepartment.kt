package com.example.springbatchstudy.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.IdClass
import java.io.Serializable
import java.time.LocalDate

@Entity
@IdClass(value = EmployeeDepartmentPK::class)
class EmployeeDepartment(
    @Id
    val employeeId: Long,

    @Id
    val departmentId: String,

    val startDate: LocalDate,

    val endDate: LocalDate,
) {
    override fun toString(): String {
        return "EmployeeDepartment(employeeId=$employeeId, departmentId='$departmentId', startDate=$startDate, endDate=$endDate)"
    }
}

data class EmployeeDepartmentPK(
    val employeeId: Long,
    val departmentId: String,
) : Serializable