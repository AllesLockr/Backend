package com.alleslocker.backend.application.vendor.usecase

import com.alleslocker.backend.application.common.InputBoundary
import com.alleslocker.backend.application.vendor.dto.response.GetImplementedVendorsResponseDto

interface GetImplementedVendorsUseCase : InputBoundary<Unit, GetImplementedVendorsResponseDto>
