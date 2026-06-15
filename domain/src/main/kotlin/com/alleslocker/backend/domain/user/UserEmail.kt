package com.alleslocker.backend.domain.user

import com.alleslocker.backend.domain.shared.Email

@JvmInline
value class UserEmail(
    val value: String,
) {
    init {
        require(value.isNotBlank()) { "Email cannot be blank" }
        require(value.length <= 255) { "Email cannot be longer than 255 characters" }
        require(Email.isValid(value)) { "Email is not valid" }
    }
}
