package com.alleslocker.backend.application.lock.usecase

import com.alleslocker.backend.application.common.InputBoundary
import com.alleslocker.backend.application.lock.dto.request.CountLocksRequestDto
import com.alleslocker.backend.application.lock.dto.response.CountLocksResponseDto

interface CountLocksUseCase : InputBoundary<CountLocksRequestDto, CountLocksResponseDto>
