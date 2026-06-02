package com.alleslocker.backend.web.user.mapper

import com.alleslocker.backend.application.user.dto.filter.UserFilterDto
import com.alleslocker.backend.web.user.schema.UserFilterSchema

fun UserFilterSchema.toDto() =
    UserFilterDto(
        search = search,
    )
