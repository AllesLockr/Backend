package com.alleslocker.backend.web.user.schema.response

import com.alleslocker.backend.web.common.model.PageSchema
import com.alleslocker.backend.web.user.schema.UserSchema

data class GetUsersPagedResponseSchema(
    val page: PageSchema<UserSchema>,
)
