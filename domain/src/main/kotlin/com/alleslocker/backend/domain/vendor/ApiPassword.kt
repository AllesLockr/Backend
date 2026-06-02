package com.alleslocker.backend.domain.vendor

@JvmInline
value class ApiPassword(
    val value: String,
) {
    init {
        require(value.isNotEmpty()) { "ApiPassword cannot be empty" }
    }
}
