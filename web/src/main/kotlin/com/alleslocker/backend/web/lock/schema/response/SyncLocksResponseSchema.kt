package com.alleslocker.backend.web.lock.schema.response

data class SyncLocksResponseSchema(
    val synced: Int,
    val deleted: Int,
)
