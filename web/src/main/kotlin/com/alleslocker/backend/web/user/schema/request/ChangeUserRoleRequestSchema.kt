package com.alleslocker.backend.web.user.schema.request

import com.alleslocker.backend.web.user.schema.UserRoleSchema

data class ChangeUserRoleRequestSchema(
    val userId: String,
    val role: UserRoleSchema,
)
