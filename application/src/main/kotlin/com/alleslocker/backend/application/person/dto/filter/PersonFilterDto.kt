package com.alleslocker.backend.application.person.dto.filter

data class PersonFilterDto(
    val search: String? = null,
    val firstname: String? = null,
    val lastname: String? = null,
    val email: String? = null
)