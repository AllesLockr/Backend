package eu.luftiger.cae.application.task.mapper

import eu.luftiger.cae.application.task.dto.TaskDTO
import eu.luftiger.cae.domain.task.Task
import eu.luftiger.cae.domain.task.TaskDescription
import eu.luftiger.cae.domain.task.TaskId
import eu.luftiger.cae.domain.task.TaskTitle

fun Task.toDTO() = TaskDTO(
    id = this.id.value,
    title = this.title.value,
    description = this.description.value,
    isCompleted = this.isCompleted
)

fun TaskDTO.toDomain() = Task(
    id = TaskId(this.id),
    title = TaskTitle(this.title),
    description = TaskDescription(this.description),
    isCompleted = this.isCompleted
)