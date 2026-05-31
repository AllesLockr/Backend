package com.alleslocker.backend.application.person.dto

import com.alleslocker.backend.application.common.dto.ExternalApiIdentityDto

data class PersonDto(
    val id: String,
    val firstname: String,
    val lastname: String,
    val email: String,
    val apiIdentities: Set<ExternalApiIdentityDto> = emptySet(),
    // TODO: Add roles
)
