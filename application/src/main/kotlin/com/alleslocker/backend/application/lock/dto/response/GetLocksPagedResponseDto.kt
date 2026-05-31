package com.alleslocker.backend.application.lock.dto.response

import com.alleslocker.backend.application.common.model.Page
import com.alleslocker.backend.application.lock.dto.LockDto

data class GetLocksPagedResponseDto(
    val page: Page<LockDto>,
)
