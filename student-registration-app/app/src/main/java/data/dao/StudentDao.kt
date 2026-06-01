package com.example.studentregistration.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.studentregistration.data.entity.Student

@Dao
interface StudentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudent(student: Student): Long

    @Update
    suspend fun updateStudent(student: Student)

    @Delete
    suspend fun deleteStudent(student: Student)

    @Query("SELECT * FROM students ORDER BY registeredAt DESC")
    fun getAllStudents(): LiveData<List<Student>>

    @Query("SELECT * FROM students WHERE id = :id")
    fun getStudentById(id: Int): LiveData<Student>

    @Query("""
        SELECT * FROM students 
        WHERE name LIKE '%' || :query || '%' 
        OR nim LIKE '%' || :query || '%'
        OR prodi LIKE '%' || :query || '%'
        ORDER BY registeredAt DESC
    """)
    fun searchStudents(query: String): LiveData<List<Student>>

    @Query("SELECT COUNT(*) FROM students")
    fun getTotalStudents(): LiveData<Int>

    @Query("SELECT COUNT(*) FROM students WHERE prodi = :prodi")
    fun getStudentCountByProdi(prodi: String): LiveData<Int>

    @Query("SELECT DISTINCT prodi FROM students ORDER BY prodi ASC")
    fun getAllProdi(): LiveData<List<String>>

    @Query("SELECT * FROM students WHERE prodi = :prodi ORDER BY name ASC")
    fun getStudentsByProdi(prodi: String): LiveData<List<Student>>

    @Query("DELETE FROM students")
    suspend fun deleteAllStudents()
}