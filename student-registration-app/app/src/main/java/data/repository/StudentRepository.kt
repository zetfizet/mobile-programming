package com.example.studentregistration.data.repository

import androidx.lifecycle.LiveData
import com.example.studentregistration.data.dao.StudentDao
import com.example.studentregistration.data.entity.Student

class StudentRepository(private val studentDao: StudentDao) {

    val allStudents: LiveData<List<Student>> = studentDao.getAllStudents()
    val totalStudents: LiveData<Int> = studentDao.getTotalStudents()
    val allProdi: LiveData<List<String>> = studentDao.getAllProdi()

    suspend fun insert(student: Student): Long {
        return studentDao.insertStudent(student)
    }

    suspend fun update(student: Student) {
        studentDao.updateStudent(student)
    }

    suspend fun delete(student: Student) {
        studentDao.deleteStudent(student)
    }

    fun getStudentById(id: Int): LiveData<Student> {
        return studentDao.getStudentById(id)
    }

    fun searchStudents(query: String): LiveData<List<Student>> {
        return studentDao.searchStudents(query)
    }

    fun getStudentsByProdi(prodi: String): LiveData<List<Student>> {
        return studentDao.getStudentsByProdi(prodi)
    }

    fun getStudentCountByProdi(prodi: String): LiveData<Int> {
        return studentDao.getStudentCountByProdi(prodi)
    }

    suspend fun deleteAll() {
        studentDao.deleteAllStudents()
    }
}