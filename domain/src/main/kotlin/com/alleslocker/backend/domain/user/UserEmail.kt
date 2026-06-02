package com.alleslocker.backend.domain.user

@JvmInline
value class UserEmail(
    val value: String,
) {
    init {
        require(value.isNotBlank()) { "Email cannot be blank" }
        require(value.length <= 255) { "Email cannot be longer than 255 characters" }
        require(EMAIL_REGEX.matches(value)) { "Email is not valid" }
    }

    companion object {
        private val EMAIL_REGEX = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")
    }
}
