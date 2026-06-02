package com.alleslocker.backend.application.vendor.usecase

import com.alleslocker.backend.application.common.InputBoundary
import com.alleslocker.backend.application.common.SuccessResponse
import com.alleslocker.backend.application.vendor.dto.request.AddVendorDataRequestDto

interface AddVendorDataUseCase : InputBoundary<AddVendorDataRequestDto, SuccessResponse>
