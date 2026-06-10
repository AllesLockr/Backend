package com.alleslocker.backend.web.accessgrant.mapper

import com.alleslocker.backend.application.accessgrant.dto.request.GetAccessGrantsPagedRequestDto
import com.alleslocker.backend.application.accessgrant.dto.request.GrantAccessRequestDto
import com.alleslocker.backend.application.accessgrant.dto.request.RevokeAccessRequestDto
import com.alleslocker.backend.application.accessgrant.dto.response.AccessGrantDto
import com.alleslocker.backend.application.accessgrant.dto.response.GetAccessGrantsPagedResponseDto
import com.alleslocker.backend.web.accessgrant.schema.request.GetAccessGrantsPagedRequestSchema
import com.alleslocker.backend.web.accessgrant.schema.request.GrantAccessRequestSchema
import com.alleslocker.backend.web.accessgrant.schema.request.RevokeAccessRequestSchema
import com.alleslocker.backend.web.accessgrant.schema.response.AccessGrantSchema
import com.alleslocker.backend.web.accessgrant.schema.response.GetAccessGrantsPagedResponseSchema
import com.alleslocker.backend.web.common.mapper.toSchema

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

fun GetAccessGrantsPagedRequestSchema.toDto() =
    GetAccessGrantsPagedRequestDto(
        page = this.page,
        size = this.size,
        personId = this.personId,
        lockId = this.lockId,
    )

fun AccessGrantDto.toSchema() =
    AccessGrantSchema(
        grantId = this.grantId,
        personId = this.personId,
        lockId = this.lockId,
        start = this.start,
        end = this.end,
        vendor = this.vendor,
        vendorExternalId = this.vendorExternalId,
    )

fun GetAccessGrantsPagedResponseDto.toSchema() =
    GetAccessGrantsPagedResponseSchema(
        page = this.page.toSchema { it.toSchema() },
    )
