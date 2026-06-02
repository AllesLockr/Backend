package com.alleslocker.backend.web.user.schema.response

import com.alleslocker.backend.web.user.schema.UserSchema

data class GetUserResponseSchema(
    val user: UserSchema,
)
