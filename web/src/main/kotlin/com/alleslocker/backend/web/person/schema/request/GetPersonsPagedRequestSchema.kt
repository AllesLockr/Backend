package com.alleslocker.backend.web.person.schema.request

import com.alleslocker.backend.application.person.dto.filter.PersonFilterDto
import com.alleslocker.backend.web.person.schema.PersonFilterSchema

data class GetPersonsPagedRequestSchema(
    val filter: PersonFilterSchema,
    val page: Int,
    val size: Int,
)
