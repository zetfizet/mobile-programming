package com.example.studentregistration.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "students")
data class Student(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nim: String,
    val name: String,
    val email: String,
    val phone: String,
    val prodi: String,
    val semester: Int,
    val address: String,
    val gender: String,
    val registeredAt: Long = System.currentTimeMillis()
)