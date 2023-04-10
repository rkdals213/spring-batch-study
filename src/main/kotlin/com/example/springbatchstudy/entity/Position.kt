package com.example.springbatchstudy.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.IdClass
import java.io.Serializable
import java.time.LocalDate

@Entity
@IdClass(value = PositionPK::class)
class Position(
    @Id
    val id: Long,

    @Id
    val positionName: String,

    @Id
    val startDate: LocalDate,

    val endDate: LocalDate?,
) {
    override fun toString(): String {
        return "Position(id=$id, positionName='$positionName', startDate=$startDate, endDate=$endDate)"
    }
}

data class PositionPK(
    val id: Long,
    val positionName: String,
    val startDate: LocalDate,
) : Serializable