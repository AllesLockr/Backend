package com.alleslocker.backend.web.accessgrant.schema.request

data class GetAccessGrantsPagedRequestSchema(
    val page: Int,
    val size: Int,
    val personId: String? = null,
    val lockId: String? = null,
)
