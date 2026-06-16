package com.alleslocker.backend.application.user.usecase

import com.alleslocker.backend.application.common.InputBoundary
import com.alleslocker.backend.application.user.dto.request.AdminResetPasswordUserRequestDto
import com.alleslocker.backend.application.user.dto.response.AdminResetPasswordUserResponseDto

interface AdminResetPasswordUserUseCase :
    InputBoundary<AdminResetPasswordUserRequestDto, AdminResetPasswordUserResponseDto>
