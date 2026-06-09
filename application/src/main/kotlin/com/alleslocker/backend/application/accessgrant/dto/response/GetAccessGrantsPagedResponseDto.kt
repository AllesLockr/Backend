package com.alleslocker.backend.application.accessgrant.dto.response

import com.alleslocker.backend.application.common.model.Page

data class GetAccessGrantsPagedResponseDto(
    val page: Page<AccessGrantDto>,
)
