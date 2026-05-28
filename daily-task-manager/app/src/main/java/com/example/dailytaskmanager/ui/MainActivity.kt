package com.example.dailytaskmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.example.dailytaskmanager.ui.TaskScreen
import com.example.dailytaskmanager.viewmodel.TaskViewModel
import com.example.dailytaskmanager.viewmodel.TaskViewModelFactory

class MainActivity : ComponentActivity() {

    private lateinit var viewModel: TaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val factory = TaskViewModelFactory(application)
        viewModel = ViewModelProvider(this, factory)[TaskViewModel::class.java]

        setContent {
            TaskScreen(viewModel)
        }
    }
}