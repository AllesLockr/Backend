package com.alleslocker.backend.application.api.gateway

import com.alleslocker.backend.application.common.gateway.ReadWriteGateway
import com.alleslocker.backend.domain.api.ApiData
import com.alleslocker.backend.domain.api.ApiId
import com.alleslocker.backend.domain.api.AvailableApis

interface ApiDataGateway : ReadWriteGateway<ApiData, ApiId> {
    fun findByForApi(forApi: AvailableApis): ApiData?
}