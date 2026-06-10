package com.alleslocker.backend.web.accessgrant.schema.response

data class RevokeAccessResponseSchema(
    val grantId: String,
    val vendor: String,
)
