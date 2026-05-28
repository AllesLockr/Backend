package com.alleslocker.backend.application.person.dto

import com.alleslocker.backend.domain.api.AvailableApis

data class PersonDto(
    val id: String,
    val firstname: String,
    val lastname: String,
    val email: String,
    val externalIds: Map<AvailableApis, String> = emptyMap(),
    //TODO: Add roles
)