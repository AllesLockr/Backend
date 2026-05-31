package com.alleslocker.backend.web.lock.schema.request

data class GetLocksPagedRequestSchema(
    val page: Int,
    val size: Int,
)
