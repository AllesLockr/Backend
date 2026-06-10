package com.alleslocker.backend.web.accessgrant.schema.response

import java.time.Instant

data class AccessGrantSchema(
    val grantId: String,
    val personId: String,
    val lockId: String,
    val start: Instant,
    val end: Instant,
    val vendor: String?,
    val vendorExternalId: String?,
)
