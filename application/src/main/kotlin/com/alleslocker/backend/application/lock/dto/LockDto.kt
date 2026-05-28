package com.alleslocker.backend.application.lock.dto

import com.alleslocker.backend.domain.api.AvailableApis

data class LockDto(
    val id: String,
    val name: String,
    val serialNumber: String,
    val tagId: Long?,
    val externalIds: Map<AvailableApis, String> = emptyMap(),
)