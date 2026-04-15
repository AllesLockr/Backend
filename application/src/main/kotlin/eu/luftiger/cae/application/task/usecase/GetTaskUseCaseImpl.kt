package eu.luftiger.cae.application.task.usecase

import eu.luftiger.cae.application.common.ErrorResponse
import eu.luftiger.cae.application.common.OutputBoundary
import eu.luftiger.cae.application.task.mapper.toDTO
import eu.luftiger.cae.domain.task.Task
import eu.luftiger.cae.domain.task.TaskDescription
import eu.luftiger.cae.domain.task.TaskId
import eu.luftiger.cae.domain.task.TaskTitle

class GetTaskUseCaseImpl : GetTaskUseCase {
    override fun execute(
        request: GetTaskRequest,
        presenter: OutputBoundary<GetTaskResponse>
    ) {
        val tasks = setOf(
            Task(
                id = TaskId("1"),
                title = TaskTitle("Sample Task 1"),
                description = TaskDescription("This is a sample task description"),
                isCompleted = false
            ),
            Task(
                id = TaskId("2"),
                title = TaskTitle("Sample Task 2"),
                description = TaskDescription("This is another sample task description."),
                isCompleted = true
            )
        )

        val task = tasks.find { it.id == TaskId(request.taskId) }
        if (task == null) {
            presenter.presentFailure(
                ErrorResponse.NotFound("Task with ID ${request.taskId} not found.")
            )
            return
        }

        presenter.present(
            GetTaskResponse(
                task = task.toDTO()
            )
        )
    }
}