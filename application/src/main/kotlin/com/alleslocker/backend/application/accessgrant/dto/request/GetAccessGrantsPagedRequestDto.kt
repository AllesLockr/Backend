package com.alleslocker.backend.application.accessgrant.dto.request

data class GetAccessGrantsPagedRequestDto(
    val page: Int,
    val size: Int,
    val personId: String? = null,
    val lockId: String? = null,
)
