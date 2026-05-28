package com.alleslocker.backend.domain.lock

@JvmInline
value class LockSerialNumber(val value: String) {
    init {
        require(value.isNotBlank()) { "LockSerialNumber cannot be blank" }
        require(value.length <= 100) { "LockSerialNumber cannot be longer than 100 characters" }
    }
}
