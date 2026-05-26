package com.alleslocker.backend.lockconnector.iseo.client

import com.alleslocker.backend.lockconnector.iseo.config.ConfigProvider
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestClient

@Component
class IseoTokenClient(private val configProvider: ConfigProvider) {

    private companion object {
        const val CLIENT_ID = "client"
        const val CLIENT_SECRET = ""
    }

    fun getToken(): IseoTokenResponse {
        val credentials = configProvider.load()
        val client = RestClient.builder().baseUrl(credentials.baseUrl).build()
        val formData = LinkedMultiValueMap<String, String>().apply{
            add("grant_type", "password")
            add("username", credentials.username)
            add("password", credentials.password)
        }
        return client.post().uri("/oauth/token")
            .headers { it.setBasicAuth(CLIENT_ID, CLIENT_SECRET) }
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .accept(MediaType.APPLICATION_JSON)
            .body(formData)
            .retrieve()
            .body(IseoTokenResponse::class.java) ?: throw IllegalStateException("Failed to get IseoTokenResponse")
    }
}