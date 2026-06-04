package com.alleslocker.backend.application.user.usecase

import com.alleslocker.backend.application.common.InputBoundary
import com.alleslocker.backend.application.user.dto.request.ResetPasswordUserRequestDto
import com.alleslocker.backend.application.user.dto.response.ResetPasswordUserResponseDto

interface ResetPasswordUserUseCase : InputBoundary<ResetPasswordUserRequestDto, ResetPasswordUserResponseDto>