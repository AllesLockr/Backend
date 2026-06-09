package com.alleslocker.backend.application.accessgrant.dto.response

import java.time.Instant

data class AccessGrantDto(
    val grantId: String,
    val personId: String,
    val lockId: String,
    val start: Instant,
    val end: Instant,
    val vendor: String?,
    val externalId: String?,
)
