package com.alleslocker.backend.application.user.usecase

import com.alleslocker.backend.application.common.InputBoundary
import com.alleslocker.backend.application.user.dto.request.EditUserRequestDto

interface EditUserUseCase : InputBoundary<EditUserRequestDto, Unit>
