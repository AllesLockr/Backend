package com.alleslocker.backend.application.person.usecase

import com.alleslocker.backend.application.common.InputBoundary
import com.alleslocker.backend.application.person.dto.request.CreatePersonRequestDto
import com.alleslocker.backend.application.person.dto.response.CreatePersonResponseDto

interface CreatePersonUseCase : InputBoundary<CreatePersonRequestDto, CreatePersonResponseDto>