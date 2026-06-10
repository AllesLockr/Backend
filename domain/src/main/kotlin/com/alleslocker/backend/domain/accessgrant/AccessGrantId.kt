package com.alleslocker.backend.domain.accessgrant

import java.util.UUID

@JvmInline
value class AccessGrantId(
    val value: String,
) {
    init {
        require(value.isNotBlank()) { "AccessGrantId cannot be blank" }
    }

    companion object {
        fun generate(): AccessGrantId = AccessGrantId(UUID.randomUUID().toString())
    }
}
