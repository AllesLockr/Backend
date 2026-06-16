package com.alleslocker.backend.application.lock.usecase

import com.alleslocker.backend.application.common.InputBoundary
import com.alleslocker.backend.application.lock.dto.LockDto
import com.alleslocker.backend.application.lock.dto.request.CreateLockRequestDto

interface CreateLockUseCase : InputBoundary<CreateLockRequestDto, LockDto>
