package com.alleslocker.backend.web.person.schema

import com.alleslocker.backend.web.common.schema.ExternalApiIdentitySchema

data class PersonSchema(
    val id: String,
    val firstname: String,
    val lastname: String,
    val email: String,
    val apiIdentities: Set<ExternalApiIdentitySchema>,
    //TODO: Add roles
)