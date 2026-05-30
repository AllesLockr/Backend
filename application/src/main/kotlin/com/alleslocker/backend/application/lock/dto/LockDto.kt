package com.alleslocker.backend.application.lock.dto

data class LockDto(
    val id: String,
    val name: String,
    val serialNumber: String,
    val tagId: Long?,
    val externalApi: String? = null,
    val externalId: String? = null,
)