package com.alleslocker.backend.lockconnector.auth.client

import com.alleslocker.backend.domain.vendor.AvailableVendors
import com.alleslocker.backend.domain.vendor.VendorAuthentication
import com.alleslocker.backend.lockconnector.auth.common.TokenClient
import com.alleslocker.backend.lockconnector.auth.common.TokenResponse
import com.alleslocker.backend.lockconnector.auth.config.ConfigProvider
import org.springframework.http.MediaType
import org.springframework.http.client.SimpleClientHttpRequestFactory
import org.springframework.web.client.RestClient

class AssaAmoqTokenClient(
    private val configProvider: ConfigProvider,
    private val api: AvailableVendors,
) : TokenClient {
    private data class LoginRequest(
        val userId: String,
        val accessKey: String,
    )

    private data class LoginResponse(
        val token: String,
        val expiresIn: Long? = null,
    )

    private val client: RestClient by lazy {
        val requestFactory =
            SimpleClientHttpRequestFactory().apply {
                setConnectTimeout(5000)
                setReadTimeout(5000)
            }
        RestClient
            .builder()
            .requestFactory(requestFactory)
            .build()
    }

    override fun getToken(): TokenResponse {
        val credentials = configProvider.load(api)
        require(credentials.authentication is VendorAuthentication.BaseAuth) {
            "This shouldn't happen: VendorAuthentification for ASSA_AMOQ should be BaseAuth!"
        }
        val apiKey =
            credentials.metadata.firstOrNull { it.key == "api-key" }?.value
                ?: throw IllegalStateException("ASSA_AMOQ vendor-data is missing the required api-key metadata field")
        val formData =
            LoginRequest(
                userId = credentials.authentication.username.value,
                accessKey = credentials.authentication.password.value,
            )
        val response =
            client
                .post()
                .uri("${credentials.baseUrl}/login")
                .header("X-Api-Key", apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(formData)
                .retrieve()
                .body(LoginResponse::class.java)
                ?: throw IllegalStateException("Failed to get $api token response")
        return TokenResponse(
            accessToken = response.token,
            expiresIn = response.expiresIn ?: DEFAULT_TOKEN_TTL_SECONDS,
        )
    }

    private companion object {
        const val DEFAULT_TOKEN_TTL_SECONDS = 3600L
    }
}
