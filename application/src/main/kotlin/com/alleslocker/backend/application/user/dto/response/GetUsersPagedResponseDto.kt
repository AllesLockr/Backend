package com.alleslocker.backend.application.user.dto.response

import com.alleslocker.backend.application.common.model.Page
import com.alleslocker.backend.application.person.dto.PersonDto
import com.alleslocker.backend.application.user.dto.UserDto

data class GetUsersPagedResponseDto(
    val page: Page<UserDto>,
)