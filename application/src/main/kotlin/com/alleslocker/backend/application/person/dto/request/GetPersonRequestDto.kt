package com.alleslocker.backend.application.person.dto.request

import com.alleslocker.backend.domain.person.PersonId

data class GetPersonRequestDto(
    val personId: String,
)
