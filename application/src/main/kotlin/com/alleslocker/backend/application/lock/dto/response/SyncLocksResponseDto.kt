package com.alleslocker.backend.application.lock.dto.response

data class SyncLocksResponseDto(
    val synced: Int,
    val deleted: Int,
)
