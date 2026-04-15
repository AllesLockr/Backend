package eu.luftiger.cae.application.user.usecase

import eu.luftiger.cae.application.common.InputBoundary
import eu.luftiger.cae.application.user.dto.request.RegisterUserRequestDto
import eu.luftiger.cae.application.user.dto.response.RegisterUserResponseDto

interface RegisterUserUseCase : InputBoundary<RegisterUserRequestDto, RegisterUserResponseDto>