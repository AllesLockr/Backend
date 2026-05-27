package com.alleslocker.backend.domain.lock

@JvmInline
value class LockName(val value: String) {
    init {
        require(value.isNotBlank()) { "LockName cannot be blank" }
        require(value.length <= 50) { "LockName cannot be longer than 50 characters" }
    }
}