package com.alleslocker.backend.domain.lock

import java.util.*

@JvmInline
value class LockId(val value: String) {
    init {
        require(value.isNotBlank()) { "LockId cannot be blank" }
    }

    companion object {
        fun generate(): LockId = LockId(UUID.randomUUID().toString())
    }
}