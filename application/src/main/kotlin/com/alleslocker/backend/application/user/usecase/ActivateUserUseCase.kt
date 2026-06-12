package com.alleslocker.backend.application.user.usecase

import com.alleslocker.backend.application.common.InputBoundary
import com.alleslocker.backend.application.user.dto.request.ActivateUserRequestDto

interface ActivateUserUseCase : InputBoundary<ActivateUserRequestDto, Unit>
