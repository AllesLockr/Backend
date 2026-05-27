package com.alleslocker.backend.web.person.schema

data class PersonFilterSchema(
    val firstname: String? = null,
    val lastname: String? = null,
    val email: String? = null
)