package com.alleslocker.backend.domain.api

@JvmInline
value class ApiUsername(
    val value: String,
) {
    init {
        require(value.isNotEmpty()) { "ApiUsername cannot be empty" }
    }
}
