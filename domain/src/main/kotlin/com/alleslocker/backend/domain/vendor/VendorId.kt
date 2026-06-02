package com.alleslocker.backend.domain.vendor

import java.util.*

@JvmInline
value class VendorId(
    val value: String,
) {
    init {
        require(value.isNotBlank()) { "ApiId cannot be blank" }
    }

    companion object {
        fun generate(): VendorId = VendorId(UUID.randomUUID().toString())
    }
}
