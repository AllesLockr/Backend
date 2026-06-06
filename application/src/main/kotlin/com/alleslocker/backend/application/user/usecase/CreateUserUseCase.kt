package com.alleslocker.backend.application.user.usecase

import com.alleslocker.backend.application.common.InputBoundary
import com.alleslocker.backend.application.user.dto.request.CreateUserRequestDto
import com.alleslocker.backend.application.user.dto.response.CreateUserResponseDto

interface CreateUserUseCase : InputBoundary<CreateUserRequestDto, CreateUserResponseDto>
