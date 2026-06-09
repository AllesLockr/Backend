package com.alleslocker.backend.web.accessgrant.mapper

import com.alleslocker.backend.application.accessgrant.dto.request.GrantAccessRequestDto
import com.alleslocker.backend.application.accessgrant.dto.request.RevokeAccessRequestDto
import com.alleslocker.backend.web.accessgrant.schema.request.GrantAccessRequestSchema
import com.alleslocker.backend.web.accessgrant.schema.request.RevokeAccessRequestSchema

fun GrantAccessRequestSchema.toDto(requesterId: String) =
    GrantAccessRequestDto(
        requesterId = requesterId,
        personId = this.personId,
        lockId = this.lockId,
        start = this.start,
        end = this.end,
    )

fun RevokeAccessRequestSchema.toDto(requesterId: String) =
    RevokeAccessRequestDto(
        requesterId = requesterId,
        grantId = this.grantId,
    )
