package com.alleslocker.backend.domain.user

@JvmInline
value class UserLastname(
    val value: String,
) {
    init {
        require(value.isNotBlank()) { "Lastname cannot be blank" }
        require(value.length <= 50) { "Lastname cannot be longer than 50 characters" }
    }
}
