package com.alleslocker.backend.application.api.mapper

import com.alleslocker.backend.application.api.dto.response.GetApiDataResponseDto
import com.alleslocker.backend.domain.api.ApiAuthentication
import com.alleslocker.backend.domain.api.ApiData

fun ApiData.toDto(): GetApiDataResponseDto {
    var apiKey: String? = null
    var apiUsername: String? = null
    var apiPassword: String? = null

    when (val auth = this.apiAuthentication) {
        is ApiAuthentication.ApiKey -> {
            apiKey = auth.value
        }

        is ApiAuthentication.BaseAuth -> {
            apiUsername = auth.username.value
            apiPassword = auth.password.value
        }
    }

    return GetApiDataResponseDto(
        id = this.id.value,
        forApi = this.forApi.toString(),
        baseUrl = this.baseUrl.toString(),
        apiKey = apiKey,
        apiUsername = apiUsername,
        apiPassword = apiPassword
    )
}