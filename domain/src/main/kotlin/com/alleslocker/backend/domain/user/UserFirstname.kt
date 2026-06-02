package com.alleslocker.backend.domain.user

@JvmInline
value class UserFirstname(
    val value: String,
) {
    init {
        require(value.isNotBlank()) { "Firstname cannot be blank" }
        require(value.length <= 50) { "Firstname cannot be longer than 50 characters" }
    }
}