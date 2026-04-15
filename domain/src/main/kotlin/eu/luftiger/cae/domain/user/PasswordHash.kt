package eu.luftiger.cae.domain.user

@JvmInline
value class PasswordHash(val value: String) {
    init {
        require(value.isNotEmpty()) { "Username cannot be empty" }
    }
}
