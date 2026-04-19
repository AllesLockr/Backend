package com.alleslocker.backend.application.person.usecase

import com.alleslocker.backend.application.common.InputBoundary
import com.alleslocker.backend.application.person.dto.request.DeletePersonRequestDto
import com.alleslocker.backend.application.person.dto.response.DeletePersonResponseDto

interface DeletePersonUseCase : InputBoundary<DeletePersonRequestDto, DeletePersonResponseDto>
