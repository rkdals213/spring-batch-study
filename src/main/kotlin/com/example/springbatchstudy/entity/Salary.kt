package com.example.springbatchstudy.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.IdClass
import java.io.Serializable
import java.time.LocalDate

@Entity
@IdClass(value = SalaryPK::class)
class Salary(
    @Id
    val id: Long,

    val annualIncome: Long,

    @Id
    val startDate: LocalDate,

    val endDate: LocalDate,

    val used: String?,
) {
}

data class SalaryPK(
    val id: Long,
    val startDate: LocalDate,
) : Serializable
