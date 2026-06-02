package com.alleslocker.backend.application.user.dto.request

import com.alleslocker.backend.application.person.dto.filter.PersonFilterDto
import com.alleslocker.backend.application.user.dto.filter.UserFilterDto

data class GetUsersPagedRequestDto(
    val requesterId: String,
    val filter: UserFilterDto,
    val page: Int,
    val size: Int,
)
