package com.alleslocker.backend.application.api.gateway

import com.alleslocker.backend.application.common.gateway.ReadWriteGateway
import com.alleslocker.backend.domain.api.ApiData
import com.alleslocker.backend.domain.api.ApiId

interface ApiDataGateway : ReadWriteGateway<ApiData, ApiId> {
}