package com.example.springbatchstudy.entity

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import java.time.LocalDate

@Entity
class Employee(
    @Id
    val id: Long,

    val birth: LocalDate,

    val lastName: String,

    val firstName: String,

    @Enumerated(value = EnumType.STRING)
    val sex: Sex,

    val joinDate: LocalDate,
) {
    override fun toString(): String {
        return "Employee(id=$id, birth=$birth, lastName='$lastName', firstName='$firstName', sex=$sex, joinDate=$joinDate)"
    }
}