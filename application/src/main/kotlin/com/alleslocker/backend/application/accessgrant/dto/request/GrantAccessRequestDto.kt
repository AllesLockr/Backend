package com.alleslocker.backend.application.accessgrant.dto.request

import java.time.Instant

data class GrantAccessRequestDto(
    val requesterId: String,
    val personId: String,
    val lockId: String,
    val start: Instant,
    val end: Instant,
)
