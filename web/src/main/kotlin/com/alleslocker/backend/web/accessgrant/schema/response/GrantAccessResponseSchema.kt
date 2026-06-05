package com.alleslocker.backend.web.accessgrant.schema.response

data class GrantAccessResponseSchema(
    val grantId: String,
    val vendor: String,
    val externalId: String,
)