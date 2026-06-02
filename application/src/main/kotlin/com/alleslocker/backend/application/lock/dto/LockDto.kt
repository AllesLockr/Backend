package com.alleslocker.backend.application.lock.dto

import com.alleslocker.backend.application.common.dto.ExternalApiIdentityDto
import com.alleslocker.backend.application.common.dto.MetadataEntryDto

data class LockDto(
    val id: String,
    val name: String,
    val serialNumber: String,
    val metadata: Set<MetadataEntryDto>,
    val apiIdentity: ExternalApiIdentityDto? = null,
)
