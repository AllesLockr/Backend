package com.alleslocker.backend.domain.lock

@JvmInline
value class LockName(val value: String) {
    init {
        require(value.isNotEmpty()) { "LockName cannot be empty" }
        require(value.length <= 100) { "LockName cannot be longer than 100 characters" }

    }
}
