package com.alleslocker.backend.application.lock.dto.response

data class FetchLocksAdapterResponse(
    val locks: List<FetchedLockDto>,
)
