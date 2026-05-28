package com.example.dailytaskmanager.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.dailytaskmanager.data.Task
import com.example.dailytaskmanager.data.TaskDatabase
import kotlinx.coroutines.launch

class TaskViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = TaskDatabase.getDatabase(application).taskDao()

    val tasks: LiveData<List<Task>> = dao.getAllTasks()

    fun addTask(title: String, deadline: Long) {
        viewModelScope.launch {
            dao.insert(Task(title = title, deadline = deadline))
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            dao.delete(task)
        }
    }

    fun toggleTask(task: Task) {
        viewModelScope.launch {
            dao.update(task.copy(isDone = !task.isDone))
        }
    }
}