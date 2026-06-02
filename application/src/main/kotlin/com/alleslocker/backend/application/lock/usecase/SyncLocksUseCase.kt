package com.alleslocker.backend.application.lock.usecase

import com.alleslocker.backend.application.common.InputBoundary
import com.alleslocker.backend.application.lock.dto.request.SyncLocksRequestDto
import com.alleslocker.backend.application.lock.dto.response.SyncLocksResponseDto

interface SyncLocksUseCase : InputBoundary<SyncLocksRequestDto, SyncLocksResponseDto>
