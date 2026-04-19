package com.alleslocker.backend.domain.role

import java.util.*

@JvmInline
value class RoleId(val value: String) {
    init {
        require(value.isNotBlank()) { "RoleId cannot be blank" }
    }

    companion object {
        fun generate(): RoleId = RoleId(UUID.randomUUID().toString())
    }
}
