package com.alleslocker.backend.application.user.usecase

import com.alleslocker.backend.application.common.InputBoundary
import com.alleslocker.backend.application.common.OutputBoundary
import com.alleslocker.backend.application.user.dto.request.RequestUserPasswordChangeRequestDto

interface RequestUserPasswordChangeUseCase : InputBoundary<RequestUserPasswordChangeRequestDto, Unit>
