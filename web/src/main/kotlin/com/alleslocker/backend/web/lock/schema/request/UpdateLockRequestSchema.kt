package com.alleslocker.backend.web.lock.schema.request

import com.alleslocker.backend.application.common.dto.ExternalApiIdentityDto
import com.alleslocker.backend.application.common.dto.MetadataEntryDto

data class UpdateLockRequestSchema(
    val name: String?,
    val serialNumber: String?,
    val metadata: Set<MetadataEntryDto>?,
    val apiIdentity: ExternalApiIdentityDto?,
)
