package com.alleslocker.backend.domain.role

@JvmInline
value class RoleColor(val value: String) {
    init {
        require(value.isNotBlank()) { "RoleColor cannot be blank" }
        require(value.matches(Regex("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$"))) { "RoleColor must be a valid hex color code" }
    }
}
