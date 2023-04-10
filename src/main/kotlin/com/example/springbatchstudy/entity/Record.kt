package com.example.springbatchstudy.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.LocalDateTime

@Entity
class Record(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    val employeeId: Long,

    val time: LocalDateTime,

    val recordSymbol: String,

    val door: String?,

    val region: String?,
) {
    override fun toString(): String {
        return "Record(id=$id, employeeId=$employeeId, time=$time, recordSymbol='$recordSymbol', door=$door, region=$region)"
    }
}
