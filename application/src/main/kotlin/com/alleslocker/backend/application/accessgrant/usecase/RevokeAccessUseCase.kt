package com.alleslocker.backend.application.accessgrant.usecase

import com.alleslocker.backend.application.accessgrant.dto.request.RevokeAccessRequestDto
import com.alleslocker.backend.application.accessgrant.dto.response.RevokeAccessResponseDto
import com.alleslocker.backend.application.common.InputBoundary

interface RevokeAccessUseCase : InputBoundary<RevokeAccessRequestDto, RevokeAccessResponseDto>
