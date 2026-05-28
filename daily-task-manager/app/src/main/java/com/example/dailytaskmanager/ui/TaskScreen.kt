package com.example.dailytaskmanager.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import com.example.dailytaskmanager.viewmodel.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class) // ✅ FIX ERROR
@Composable
fun TaskScreen(viewModel: TaskViewModel) {

    val tasks by viewModel.tasks.observeAsState(emptyList())
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Daily Task Manager") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Text("+")
            }
        }
    ) { padding ->

        if (tasks.isEmpty()) {
            Box(modifier = Modifier.padding(padding)) {
                Text("Belum ada task 😴")
            }
        } else {
            LazyColumn(modifier = Modifier.padding(padding)) {
                items(tasks) { task ->
                    TaskItem(
                        task = task,
                        onCheckedChange = { viewModel.toggleTask(task) },
                        onDelete = { viewModel.deleteTask(task) }
                    )
                }
            }
        }

        if (showDialog) {
            AddTaskDialog(
                onDismiss = { showDialog = false },
                onAdd = { title, deadline ->
                    viewModel.addTask(title, deadline)
                    showDialog = false
                }
            )
        }
    }
}