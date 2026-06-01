package com.example.studentregistration.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.switchMap
import com.example.studentregistration.data.database.StudentDatabase
import com.example.studentregistration.data.entity.Student
import com.example.studentregistration.data.repository.StudentRepository
import kotlinx.coroutines.launch

class StudentViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: StudentRepository
    val allStudents: LiveData<List<Student>>
    val totalStudents: LiveData<Int>
    val allProdi: LiveData<List<String>>

    private val _searchQuery = MutableLiveData<String>("")
    val searchQuery: LiveData<String> = _searchQuery

    // Lazy initialization for displayedStudents
    val displayedStudents: LiveData<List<Student>> by lazy {
        _searchQuery.switchMap { query ->
            if (query.isNullOrBlank()) {
                repository.allStudents
            } else {
                repository.searchStudents(query)
            }
        }
    }

    private val _insertResult = MutableLiveData<Long?>()
    val insertResult: LiveData<Long?> = _insertResult

    private val _operationSuccess = MutableLiveData<Boolean>(false)
    val operationSuccess: LiveData<Boolean> = _operationSuccess

    init {
        val studentDao = StudentDatabase.getDatabase(application).studentDao()
        repository = StudentRepository(studentDao)
        allStudents = repository.allStudents
        totalStudents = repository.totalStudents
        allProdi = repository.allProdi
    }

    fun resetOperationStatus() {
        _operationSuccess.value = false
        _insertResult.value = null
    }

    fun insert(student: Student) = viewModelScope.launch {
        val result = repository.insert(student)
        _insertResult.postValue(result)
        _operationSuccess.postValue(true)
    }

    fun update(student: Student) = viewModelScope.launch {
        repository.update(student)
        _operationSuccess.postValue(true)
    }

    fun delete(student: Student) = viewModelScope.launch {
        repository.delete(student)
        _operationSuccess.postValue(true)
    }

    fun getStudentById(id: Int): LiveData<Student> {
        return repository.getStudentById(id)
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun getStudentsByProdi(prodi: String): LiveData<List<Student>> {
        return repository.getStudentsByProdi(prodi)
    }

    fun getStudentCountByProdi(prodi: String): LiveData<Int> {
        return repository.getStudentCountByProdi(prodi)
    }

    fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
    }
}

class StudentViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StudentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StudentViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}