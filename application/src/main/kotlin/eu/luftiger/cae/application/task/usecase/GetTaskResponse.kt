package eu.luftiger.cae.application.task.usecase

import eu.luftiger.cae.application.task.dto.TaskDTO

data class GetTaskResponse(
    val task: TaskDTO
)