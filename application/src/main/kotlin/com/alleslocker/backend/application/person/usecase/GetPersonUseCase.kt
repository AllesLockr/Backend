package com.alleslocker.backend.application.person.usecase

import com.alleslocker.backend.application.common.InputBoundary
import com.alleslocker.backend.application.person.dto.request.GetPersonRequestDto
import com.alleslocker.backend.application.person.dto.response.GetPersonResponseDto

interface GetPersonUseCase : InputBoundary<GetPersonRequestDto, GetPersonResponseDto>
