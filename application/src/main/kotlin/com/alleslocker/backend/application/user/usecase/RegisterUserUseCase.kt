package com.alleslocker.backend.application.user.usecase

import com.alleslocker.backend.application.common.InputBoundary
import com.alleslocker.backend.application.user.dto.request.RegisterUserRequestDto
import com.alleslocker.backend.application.user.dto.response.RegisterUserResponseDto

interface RegisterUserUseCase :
    com.alleslocker.backend.application.common.InputBoundary<com.alleslocker.backend.application.user.dto.request.RegisterUserRequestDto, com.alleslocker.backend.application.user.dto.response.RegisterUserResponseDto>