package com.alleslocker.backend.lockconnector.accessgrant.client

import com.alleslocker.backend.application.accessgrant.dto.request.adapter.GrantAccessAdapterRequest
import com.alleslocker.backend.application.accessgrant.dto.request.adapter.RevokeAccessAdapterRequest
import com.alleslocker.backend.application.accessgrant.dto.response.GrantAccessAdapterResponse
import com.alleslocker.backend.domain.vendor.AvailableVendors
import com.alleslocker.backend.lockconnector.accessgrant.adapter.AccessGrantClient
import com.alleslocker.backend.lockconnector.common.GenericRestClient

internal class AssaAccessGrantClientImpl(
    private val restClient: GenericRestClient,
) : AccessGrantClient {
    override val vendor = AvailableVendors.ASSA_AMOQ

    override fun grant(request: GrantAccessAdapterRequest): GrantAccessAdapterResponse =
        TODO("ASSA access grant not yet implemented")

    override fun revoke(request: RevokeAccessAdapterRequest): Unit = TODO("ASSA access revoke not yet implemented")
}
