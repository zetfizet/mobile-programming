package com.example.dailytaskmanager.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.dailytaskmanager.data.Task

import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TaskItem(
    task: Task,
    onCheckedChange: () -> Unit,
    onDelete: () -> Unit
) {
    // FORMAT DEADLINE (tanggal + jam)
    val formattedDeadline = try {
        val formatter = SimpleDateFormat("EEE, dd MMM yyyy HH:mm", Locale.getDefault())
        formatter.format(Date(task.deadline))
    } catch (e: Exception) {
        "No deadline"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Row(modifier = Modifier.weight(1f)) {

                Checkbox(
                    checked = task.isDone,
                    onCheckedChange = { onCheckedChange() }
                )

                Column {
                    Text(
                        text = task.title,
                        style = MaterialTheme.typography.titleMedium,
                        textDecoration = if (task.isDone)
                            TextDecoration.LineThrough
                        else null
                    )

                    Text(
                        text = "Deadline: $formattedDeadline",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            TextButton(onClick = onDelete) {
                Text("Delete")
            }
        }
    }
}