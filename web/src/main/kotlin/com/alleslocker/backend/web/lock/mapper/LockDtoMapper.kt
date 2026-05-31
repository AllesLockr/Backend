package com.alleslocker.backend.web.lock.mapper

import com.alleslocker.backend.application.lock.dto.LockDto
import com.alleslocker.backend.web.common.schema.ExternalApiIdentitySchema
import com.alleslocker.backend.web.lock.schema.LockSchema

fun LockDto.toSchema() =
    LockSchema(
        id = this.id,
        name = this.name,
        serialNumber = this.serialNumber,
        tagId = this.tagId,
        apiIdentity = this.apiIdentity?.let { ExternalApiIdentitySchema(api = it.api, externalId = it.externalId) },
    )
