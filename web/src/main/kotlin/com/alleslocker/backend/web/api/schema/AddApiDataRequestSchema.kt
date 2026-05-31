package com.alleslocker.backend.web.api.schema

data class AddApiDataRequestSchema(
    val forApi: String,
    val baseUrl: String,
    val apiKey: String?,
    val apiUsername: String?,
    val apiPassword: String?,
)
