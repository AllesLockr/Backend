package com.alleslocker.backend.web.person.mapper

import com.alleslocker.backend.application.person.dto.request.GetPersonsPagedRequestDto
import com.alleslocker.backend.application.person.dto.response.GetPersonsPagedResponseDto
import com.alleslocker.backend.web.common.mapper.toSchema
import com.alleslocker.backend.web.person.schema.request.GetPersonsPagedRequestSchema
import com.alleslocker.backend.web.person.schema.response.GetPersonsPagedResponseSchema

fun GetPersonsPagedRequestSchema.toDto() =
    GetPersonsPagedRequestDto(
        filter = this.filter.toDto(),
        page = this.page,
        size = this.size,
    )

fun GetPersonsPagedResponseDto.toSchema() =
    GetPersonsPagedResponseSchema(
        page = this.page.toSchema { it.toSchema() },
    )
