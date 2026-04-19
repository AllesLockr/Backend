package com.alleslocker.backend.domain.role

@JvmInline
value class RoleDescription(val value: String) {
    init {
        require(value.isNotBlank()) { "RoleDescription cannot be blank" }
        require(value.length <= 255) { "RoleDescription cannot be longer than 255 characters" }
    }
}
