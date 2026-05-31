package com.alleslocker.backend.application.lock.usecase

import com.alleslocker.backend.application.common.InputBoundary
import com.alleslocker.backend.application.lock.dto.request.GetLocksPagedRequestDto
import com.alleslocker.backend.application.lock.dto.response.GetLocksPagedResponseDto

interface GetLocksPagedUseCase : InputBoundary<GetLocksPagedRequestDto, GetLocksPagedResponseDto>