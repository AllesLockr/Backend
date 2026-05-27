package com.alleslocker.backend.web.person.schema

data class PersonFilterSchema(
    val search: String? = null,
    val firstname: String? = null,
    val lastname: String? = null,
    val email: String? = null
)