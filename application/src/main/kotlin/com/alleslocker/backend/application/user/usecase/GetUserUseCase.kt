package com.alleslocker.backend.application.user.usecase

import com.alleslocker.backend.application.common.InputBoundary
import com.alleslocker.backend.application.user.dto.request.GetUserRequestDto
import com.alleslocker.backend.application.user.dto.response.GetUserResponseDto

interface GetUserUseCase : InputBoundary<GetUserRequestDto, GetUserResponseDto>
