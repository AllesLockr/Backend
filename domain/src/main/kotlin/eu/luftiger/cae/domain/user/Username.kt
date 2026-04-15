package eu.luftiger.cae.domain.user

@JvmInline
value class Username(val value: String) {
    init {
        require(value.isNotEmpty()) { "Username cannot be empty" }
        require(value.length in 3..30) { "Username must be between 3 and 30 characters long" }
        require(value.all { it.isLetterOrDigit() || it == '_' }) { "Username can only contain letters, digits, and underscores" }
    }
}