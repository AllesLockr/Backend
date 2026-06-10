package com.alleslocker.backend.application.accessgrant.adapter

import com.alleslocker.backend.application.accessgrant.dto.request.adapter.GrantAccessAdapterRequest
import com.alleslocker.backend.application.accessgrant.dto.request.adapter.RevokeAccessAdapterRequest
import com.alleslocker.backend.application.accessgrant.dto.response.GrantAccessAdapterResponse
import com.alleslocker.backend.application.common.adapter.Adapter

interface AccessGrantAdapter : Adapter {
    fun grant(request: GrantAccessAdapterRequest): GrantAccessAdapterResponse

    fun revoke(request: RevokeAccessAdapterRequest)
}
