package com.alleslocker.backend.lockconnector.auth.client

import com.alleslocker.backend.domain.vendor.AvailableVendors
import com.alleslocker.backend.domain.vendor.VendorAuthentication
import com.alleslocker.backend.lockconnector.auth.common.TokenClient
import com.alleslocker.backend.lockconnector.auth.common.TokenResponse
import com.alleslocker.backend.lockconnector.auth.config.ConfigProvider
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.http.MediaType
import org.springframework.http.client.SimpleClientHttpRequestFactory
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestClient

class IseoOAuthTokenClient(
    private val configProvider: ConfigProvider,
    private val api: AvailableVendors,
    private val clientId: String = "client",
    private val clientSecret: String = "",
) : TokenClient {
    private data class OAuthResponse(
        @JsonProperty("access_token") val accessToken: String,
        @JsonProperty("expires_in") val expiresIn: Long,
    )

    private val credentials by lazy { configProvider.load(api) }

    private val client: RestClient by lazy {
        val requestFactory =
            SimpleClientHttpRequestFactory().apply {
                setConnectTimeout(5000)
                setReadTimeout(5000)
            }
        RestClient
            .builder()
            .requestFactory(requestFactory)
            .baseUrl(credentials.baseUrl)
            .build()
    }

    override fun getToken(): TokenResponse {
        val credentials = configProvider.load(api)
        require(credentials.authentication is VendorAuthentication.BaseAuth) {
            "This shouldn't happen: VendorAuthentication for ISEO should be BaseAuth!"
        }
        val formData =
            LinkedMultiValueMap<String, String>().apply {
                add("grant_type", "password")
                add("username", credentials.authentication.username.value)
                add("password", credentials.authentication.password.value)
            }
        val response =
            client
                .post()
                .uri("/oauth/token")
                .headers { it.setBasicAuth(clientId, clientSecret) }
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON)
                .body(formData)
                .retrieve()
                .body(OAuthResponse::class.java)
                ?: throw IllegalStateException("Failed to get $api token response")
        return TokenResponse(accessToken = response.accessToken, expiresIn = response.expiresIn)
    }
}
