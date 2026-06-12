package com.alleslocker.backend.web.user.schema.request

import com.alleslocker.backend.application.user.dto.UserRoleDto
import com.alleslocker.backend.web.user.schema.UserRoleSchema

data class ChangeUserRoleRequestSchema(
    val userId: String,
    val role: UserRoleSchema,
)
