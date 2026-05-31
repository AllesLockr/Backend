package com.alleslocker.backend.application.person.usecase

import com.alleslocker.backend.application.common.InputBoundary
import com.alleslocker.backend.application.person.dto.request.GetPersonsPagedRequestDto
import com.alleslocker.backend.application.person.dto.response.GetPersonsPagedResponseDto

interface GetPersonsPagedUseCase : InputBoundary<GetPersonsPagedRequestDto, GetPersonsPagedResponseDto>
