package com.alleslocker.backend.application.lock.usecase

import com.alleslocker.backend.application.common.InputBoundary
import com.alleslocker.backend.application.lock.dto.LockDto
import com.alleslocker.backend.application.lock.dto.request.UpdateLockRequestDto

interface UpdateLockUseCase : InputBoundary<UpdateLockRequestDto, LockDto>
