package com.alleslocker.backend.web.vendor.schema

data class AddVendorDataRequestSchema(
    val forApi: String,
    val baseUrl: String,
    val apiKey: String?,
    val apiUsername: String?,
    val apiPassword: String?,
)
