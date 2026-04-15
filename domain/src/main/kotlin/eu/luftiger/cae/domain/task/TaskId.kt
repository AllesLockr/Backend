package eu.luftiger.cae.domain.task

@JvmInline
value class TaskId(val value: String) {
    init {
        require(value.isNotEmpty()) { "TaskId cannot be empty" }
    }
}
