package com.alleslocker.backend.application.api.usecase

import com.alleslocker.backend.application.api.dto.response.GetImplementedApisResponseDto
import com.alleslocker.backend.application.common.InputBoundary

interface GetImplementedApisUseCase : InputBoundary<Unit, GetImplementedApisResponseDto> {
}