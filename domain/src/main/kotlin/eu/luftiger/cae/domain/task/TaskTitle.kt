package eu.luftiger.cae.domain.task

@JvmInline
value class TaskTitle(val value: String) {
    init {
        require(value.isNotEmpty()) { "Task title cannot be empty" }
        require(value.length <= 100) { "Task title cannot exceed 100 characters" }
    }
}
