package com.alleslocker.backend.application.person.usecase

import com.alleslocker.backend.application.common.InputBoundary
import com.alleslocker.backend.application.person.dto.request.CountPersonsRequestDto
import com.alleslocker.backend.application.person.dto.response.CountPersonsResponseDto

interface CountPersonsUseCase : InputBoundary<CountPersonsRequestDto, CountPersonsResponseDto>
