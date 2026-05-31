package com.alleslocker.backend.web.lock.schema

import com.alleslocker.backend.web.common.schema.ExternalApiIdentitySchema

data class LockSchema(
    val id: String,
    val name: String,
    val serialNumber: String,
    val tagId: Long?,
    val apiIdentity: ExternalApiIdentitySchema?,
)