package com.alleslocker.backend.application.vendor.usecase

import com.alleslocker.backend.application.common.InputBoundary
import com.alleslocker.backend.application.vendor.dto.response.GetVendorDataResponseDto

interface GetAllVendorDataUseCase : InputBoundary<Unit, List<GetVendorDataResponseDto>>
