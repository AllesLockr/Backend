package com.alleslocker.backend.lockconnector.accessgrant.adapter

import com.alleslocker.backend.application.accessgrant.dto.request.adapter.GrantAccessAdapterRequest
import com.alleslocker.backend.application.accessgrant.dto.request.adapter.RevokeAccessAdapterRequest
import com.alleslocker.backend.application.accessgrant.dto.response.GrantAccessAdapterResponse
import com.alleslocker.backend.domain.vendor.AvailableVendors

internal interface AccessGrantClient {
    val vendor: AvailableVendors

    fun grant(request: GrantAccessAdapterRequest): GrantAccessAdapterResponse

    fun revoke(request: RevokeAccessAdapterRequest)
}
