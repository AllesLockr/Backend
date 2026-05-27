package com.alleslocker.backend.application.person.dto.response

import com.alleslocker.backend.application.common.model.Page
import com.alleslocker.backend.application.person.dto.PersonDto

data class GetPersonsPagedResponseDto(
    val pages: Page<PersonDto>
)