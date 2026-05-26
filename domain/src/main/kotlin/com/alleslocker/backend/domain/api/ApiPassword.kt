package com.alleslocker.backend.domain.api

@JvmInline
value class ApiPassword(val value: String) {
    init {
        require(value.isNotEmpty()) { "ApiPassword cannot be empty" }
    }
}