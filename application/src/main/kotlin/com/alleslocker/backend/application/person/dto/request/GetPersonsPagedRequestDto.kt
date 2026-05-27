package com.alleslocker.backend.application.person.dto.request

import com.alleslocker.backend.application.person.dto.filter.PersonFilterDto

data class GetPersonsPagedRequestDto(
    val filter: PersonFilterDto,
    val page: Int,
    val size: Int,
)