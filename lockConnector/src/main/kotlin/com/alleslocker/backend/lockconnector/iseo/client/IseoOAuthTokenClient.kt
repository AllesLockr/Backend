package com.alleslocker.backend.lockconnector.iseo.client

import com.alleslocker.backend.domain.api.AvailableApis
import com.alleslocker.backend.lockconnector.client.TokenClient
import com.alleslocker.backend.lockconnector.client.TokenResponse
import com.alleslocker.backend.lockconnector.iseo.config.ConfigProvider
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.http.MediaType
import org.springframework.http.client.SimpleClientHttpRequestFactory
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestClient

class IseoOAuthTokenClient(
    private val configProvider: ConfigProvider,
    private val api: AvailableApis,
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
        val formData =
            LinkedMultiValueMap<String, String>().apply {
                add("grant_type", "password")
                add("username", credentials.username)
                add("password", credentials.password)
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
