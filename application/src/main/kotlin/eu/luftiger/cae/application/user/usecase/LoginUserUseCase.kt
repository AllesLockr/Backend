package eu.luftiger.cae.application.user.usecase

import eu.luftiger.cae.application.common.InputBoundary
import eu.luftiger.cae.application.user.dto.request.LoginUserRequestDto
import eu.luftiger.cae.application.user.dto.response.LoginUserResponseDto

interface LoginUserUseCase : InputBoundary<LoginUserRequestDto, LoginUserResponseDto>