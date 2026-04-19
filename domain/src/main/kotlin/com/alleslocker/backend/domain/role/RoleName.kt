package com.alleslocker.backend.domain.role

@JvmInline
value class RoleName(val value: String) {
    init {
        require(value.isNotBlank()) { "RoleName cannot be blank" }
        require(value.length <= 50) { "RoleName cannot be longer than 50 characters" }
    }
}
