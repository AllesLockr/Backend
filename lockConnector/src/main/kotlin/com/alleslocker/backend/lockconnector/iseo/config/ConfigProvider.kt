package com.alleslocker.backend.lockconnector.iseo.config

import com.alleslocker.backend.application.api.gateway.ApiDataGateway
import com.alleslocker.backend.domain.api.ApiAuthentication
import com.alleslocker.backend.domain.api.AvailableApis
import org.springframework.stereotype.Component

@Component
class ConfigProvider(
    private val apiGateway: ApiDataGateway,
) {
    data class ApiCredentials(
        val baseUrl: String,
        val username: String,
        val password: String,
    )

    fun load(api: AvailableApis): ApiCredentials {
        val apiData = apiGateway.findByForApi(api) ?: throw IllegalStateException("$api API data not found")
        val auth = apiData.apiAuthentication
        require(auth is ApiAuthentication.BaseAuth) { "$api API data must use base auth" }
        return ApiCredentials(
            baseUrl = apiData.baseUrl.toString(),
            username = auth.username.value,
            password = auth.password.value,
        )
    }
}
