package com.alleslocker.backend.application.lock.dto.request

data class GetLocksPagedRequestDto(
    val page: Int,
    val size: Int,
)