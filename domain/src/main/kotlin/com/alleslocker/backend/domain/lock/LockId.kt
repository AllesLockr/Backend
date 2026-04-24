package com.alleslocker.backend.domain.lock

import java.util.UUID

@JvmInline
value class LockId(val value: String) {
    init {
        require(value.isNotBlank()) { "ID can't be blank" }
    }
    companion object {
        fun generate(): LockId = LockId(UUID.randomUUID().toString())
    }
}
