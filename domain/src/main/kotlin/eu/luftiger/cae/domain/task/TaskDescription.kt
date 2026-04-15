package eu.luftiger.cae.domain.task

@JvmInline
value class TaskDescription(val value: String) {
    init {
        require(value.isNotEmpty()) { "Description cannot be empty" }
        require(value.length <= 500) { "Description cannot be longer than 500 characters" }
    }
}
