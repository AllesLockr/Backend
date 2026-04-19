package com.alleslocker.backend.domain.task

data class Task (
    val id: TaskId,
    val title: TaskTitle,
    val description: TaskDescription,
    val isCompleted: Boolean
)