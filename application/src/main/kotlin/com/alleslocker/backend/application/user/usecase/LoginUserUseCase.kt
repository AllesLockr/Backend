package com.alleslocker.backend.application.user.usecase

import com.alleslocker.backend.application.common.InputBoundary
import com.alleslocker.backend.application.user.dto.request.LoginUserRequestDto
import com.alleslocker.backend.application.user.dto.response.LoginUserResponseDto

interface LoginUserUseCase : InputBoundary<LoginUserRequestDto, LoginUserResponseDto>