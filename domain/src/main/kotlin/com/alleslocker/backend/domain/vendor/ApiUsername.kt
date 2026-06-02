package com.alleslocker.backend.domain.vendor

@JvmInline
value class ApiUsername(
    val value: String,
) {
    init {
        require(value.isNotEmpty()) { "ApiUsername cannot be empty" }
    }
}
