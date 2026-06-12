package com.alleslocker.backend.application.user.dto.request

import com.alleslocker.backend.application.user.dto.UserRoleDto

data class ChangeUserRoleRequestDto(
    val requestorId: String,
    val userId: String,
    val role: UserRoleDto,
)
