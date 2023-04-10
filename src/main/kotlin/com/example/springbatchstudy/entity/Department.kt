package com.example.springbatchstudy.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
class Department(
    @Id
    val id: String,

    val departmentName: String,

    val note: String?
) {
    override fun toString(): String {
        return "Department(id='$id', departmentName='$departmentName', note='$note')"
    }
}