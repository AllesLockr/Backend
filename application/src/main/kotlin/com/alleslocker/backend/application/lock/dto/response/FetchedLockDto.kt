package com.alleslocker.backend.application.lock.dto.response

data class FetchedLockDto(
    val name: String,
    val serialNumber: String,
    val tagId: Long?,
)
