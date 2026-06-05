package com.alleslocker.backend.web.accessgrant.schema.request

import java.time.Instant

data class GrantAccessRequestSchema(
    val personId: String,
    val lockId: String,
    val start: Instant,
    val end: Instant,
)