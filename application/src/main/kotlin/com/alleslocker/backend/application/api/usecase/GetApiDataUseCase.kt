package com.alleslocker.backend.application.api.usecase

import com.alleslocker.backend.application.api.dto.response.GetApiDataResponseDto
import com.alleslocker.backend.application.common.IdRequest
import com.alleslocker.backend.application.common.InputBoundary

interface GetApiDataUseCase : InputBoundary<IdRequest, GetApiDataResponseDto>
