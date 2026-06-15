package com.alleslocker.backend.web.vendor.schema

import com.alleslocker.backend.web.common.schema.MetadataEntrySchema

data class AddVendorDataRequestSchema(
    val forApi: String,
    val baseUrl: String,
    val apiKey: String?,
    val apiUsername: String?,
    val apiPassword: String?,
    val metadata: Set<MetadataEntrySchema>?,
)
