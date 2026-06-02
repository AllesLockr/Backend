package com.alleslocker.backend.web.lock.schema

import com.alleslocker.backend.web.common.schema.ExternalApiIdentitySchema
import com.alleslocker.backend.web.common.schema.MetadataEntrySchema

data class LockSchema(
    val id: String,
    val name: String,
    val serialNumber: String,
    val metadata: Set<MetadataEntrySchema>,
    val apiIdentity: ExternalApiIdentitySchema?,
)
