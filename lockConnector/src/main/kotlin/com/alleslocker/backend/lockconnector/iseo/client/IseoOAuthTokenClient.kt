package com.alleslocker.backend.lockconnector.iseo.client

import com.alleslocker.backend.domain.api.AvailableApis
import com.alleslocker.backend.lockconnector.client.TokenClient
import com.alleslocker.backend.lockconnector.client.TokenResponse
import com.alleslocker.backend.lockconnector.iseo.config.ConfigProvider
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.http.MediaType
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestClient

class IseoOAuthTokenClient(
    private val configProvider: ConfigProvider,
    private val api: AvailableApis,
) : TokenClient {
    private data class OAuthResponse(
        @JsonProperty("access_token") val accessToken: String,
        @JsonProperty("expires_in") val expiresIn: Long,
    )

    private companion object {
        const val CLIENT_ID = "client"
        const val CLIENT_SECRET = ""
    }

    override fun getToken(): TokenResponse {
        val credentials = configProvider.load(api)
        val client = RestClient.builder().baseUrl(credentials.baseUrl).build()
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
                .headers { it.setBasicAuth(CLIENT_ID, CLIENT_SECRET) }
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON)
                .body(formData)
                .retrieve()
                .body(OAuthResponse::class.java)
                ?: throw IllegalStateException("Failed to get $api token response")
        return TokenResponse(accessToken = response.accessToken, expiresIn = response.expiresIn)
    }
}
