package eu.luftiger.cae.web.user.mapper

import eu.luftiger.cae.application.user.dto.request.LoginUserRequestDto
import eu.luftiger.cae.application.user.dto.request.RegisterUserRequestDto
import eu.luftiger.cae.application.user.dto.response.RegisterUserResponseDto
import eu.luftiger.cae.web.user.schema.request.LoginUserRequestSchema
import eu.luftiger.cae.web.user.schema.request.RegisterUserRequestSchema
import eu.luftiger.cae.web.user.schema.response.RegisterUserResponseSchema

fun LoginUserRequestSchema.toDto() = LoginUserRequestDto(
    username = this.username,
    password = this.password
)