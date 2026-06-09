package com.alleslocker.backend.application.vendor.usecase

import com.alleslocker.backend.application.common.InputBoundary
import com.alleslocker.backend.application.common.SuccessResponse
import com.alleslocker.backend.application.vendor.dto.request.DeleteVendorDataRequestDto

interface DeleteVendorDataUseCase : InputBoundary<DeleteVendorDataRequestDto, SuccessResponse> {
}