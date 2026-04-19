package com.alleslocker.backend.domain.user

@JvmInline
value class PasswordHash(val value: String) {
    init {
        require(value.isNotEmpty()) { "Username cannot be empty" }
    }
}
