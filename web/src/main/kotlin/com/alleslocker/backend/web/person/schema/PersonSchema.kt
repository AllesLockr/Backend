package com.alleslocker.backend.web.person.schema

data class PersonSchema(
    val id: String,
    val firstname: String,
    val lastname: String,
    val email: String,
    //TODO: Add roles
)