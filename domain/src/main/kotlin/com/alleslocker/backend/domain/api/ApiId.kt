package com.alleslocker.backend.domain.api

import java.util.*

@JvmInline
value class ApiId(
    val value: String,
) {
    init {
        require(value.isNotBlank()) { "ApiId cannot be blank" }
    }

    companion object {
        fun generate(): ApiId = ApiId(UUID.randomUUID().toString())
    }
}
