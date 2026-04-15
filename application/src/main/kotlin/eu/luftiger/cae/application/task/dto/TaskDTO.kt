package eu.luftiger.cae.application.task.dto

data class TaskDTO(
    val id: String,
    val title: String,
    val description: String,
    val isCompleted: Boolean
)