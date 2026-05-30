package com.alleslocker.backend.web.lock.schema

data class LockSchema(
    val id: String,
    val name: String,
    val serialNumber: String,
    val tagId: Long?,
    val externalApi: String?,
    val externalId: String?,
)