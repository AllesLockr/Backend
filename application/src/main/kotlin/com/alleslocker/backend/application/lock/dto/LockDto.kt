package com.alleslocker.backend.application.lock.dto

import com.alleslocker.backend.application.common.dto.ExternalApiIdentityDto

data class LockDto(
    val id: String,
    val name: String,
    val serialNumber: String,
    val tagId: Long?,
    val apiIdentity: ExternalApiIdentityDto? = null,
)
