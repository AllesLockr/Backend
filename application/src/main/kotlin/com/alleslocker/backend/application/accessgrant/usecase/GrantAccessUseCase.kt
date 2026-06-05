package com.alleslocker.backend.application.accessgrant.usecase

import com.alleslocker.backend.application.accessgrant.dto.request.GrantAccessRequestDto
import com.alleslocker.backend.application.accessgrant.dto.response.GrantAccessResponseDto
import com.alleslocker.backend.application.common.InputBoundary

interface GrantAccessUseCase : InputBoundary<GrantAccessRequestDto, GrantAccessResponseDto>
