package com.alleslocker.backend.lockconnector.iseo.client

import com.alleslocker.backend.lockconnector.iseo.config.IseoConfig
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestClient

@Component
class IseoTokenClient(private val config: IseoConfig) {
    private val client by lazy {
        RestClient.builder().baseUrl(config.baseUrl).build()
    }

    fun getToken(): IseoTokenResponse {
        val formData = LinkedMultiValueMap<String, String>().apply{
            add("grant_type", "password")
            add("username", config.username)
            add("password", config.password)
        }
        return client.post().uri("/oauth/token")
            .headers { it.setBasicAuth(config.clientId, config.clientSecret) }
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(formData)
            .retrieve()
            .body(IseoTokenResponse::class.java) ?: throw IllegalStateException("Failed to get IseoTokenResponse")
    }
}