package com.alleslocker.backend.web.user.schema.request

import com.alleslocker.backend.application.user.dto.UserRoleDto

data class ChangeUserRoleRequestSchema(
    val userId: String,
    val role: UserRoleDto,
)
