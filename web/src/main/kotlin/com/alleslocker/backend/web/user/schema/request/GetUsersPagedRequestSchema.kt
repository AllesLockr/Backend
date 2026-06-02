package com.alleslocker.backend.web.user.schema.request

import com.alleslocker.backend.web.user.schema.UserFilterSchema

data class GetUsersPagedRequestSchema(
    val filter: UserFilterSchema,
    val page: Int,
    val size: Int,
)
