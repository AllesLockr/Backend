package com.alleslocker.backend.domain.lock

@JvmInline
value class LockDescription(val value: String) {
    init {
        require(value.length <= 500) { "Description cannot be longer than 500" }
    }
}
