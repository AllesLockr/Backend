package com.alleslocker.backend.domain.accessgrant

import java.time.Instant

data class AccessSchedule(
    val start: Instant,
    val end: Instant,
) {
    init {
        require(end.isAfter(start)) { "Schedule end must be after start" }
    }
}
