package com.alleslocker.backend.application.api.usecase

import com.alleslocker.backend.application.api.dto.request.AddApiDataRequestDto
import com.alleslocker.backend.application.common.InputBoundary
import com.alleslocker.backend.application.common.SuccessResponse

interface AddApiDataUseCase : InputBoundary<AddApiDataRequestDto, SuccessResponse> {
}