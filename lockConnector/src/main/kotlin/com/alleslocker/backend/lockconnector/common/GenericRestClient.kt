package com.alleslocker.backend.lockconnector.common

import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

@Component
class GenericRestClient {
    val client: RestClient by lazy {
        RestClient
            .builder()
            .build()
    }

    fun post(
        endpoint: String,
        body: Any,
        headers: Map<String, String> = emptyMap(),
    ) {
        client
            .post()
            .uri(endpoint)
            .contentType(MediaType.APPLICATION_JSON)
            .headers { header ->
                headers.forEach { (k, v) ->
                    header.set(k, v)
                }
            }.body(body)
            .retrieve()
            .toBodilessEntity()
    }

    fun postForResponse(
        endpoint: String,
        body: Any,
        headers: Map<String, String> = emptyMap(),
        contentType: MediaType,
    ): RestClient.ResponseSpec =
        client
            .post()
            .uri(endpoint)
            .contentType(contentType)
            .headers { header ->
                headers.forEach { (k, v) ->
                    header.set(k, v)
                }
            }.body(body)
            .retrieve()

    fun putForResponse(
        endpoint: String,
        body: Any,
        headers: Map<String, String> = emptyMap(),
        contentType: MediaType,
    ): RestClient.ResponseSpec =
        client
            .put()
            .uri(endpoint)
            .contentType(contentType)
            .headers { header ->
                headers.forEach { (k, v) ->
                    header.set(k, v)
                }
            }.body(body)
            .retrieve()

    fun delete(
        endpoint: String,
        headers: Map<String, String> = emptyMap(),
    ) {
        client
            .delete()
            .uri(endpoint)
            .headers { header -> headers.forEach { (k, v) -> header.set(k, v) } }
            .retrieve()
            .toBodilessEntity()
    }

    inline fun <reified T> get(
        endpoint: String,
        headers: Map<String, String> = emptyMap(),
    ): T? =
        client
            .get()
            .uri(endpoint)
            .headers { header ->
                headers.forEach { (k, v) -> header.set(k, v) }
            }.accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .body(object : ParameterizedTypeReference<T>() {})

    fun getBodiless(
        endpoint: String,
        headers: Map<String, String> = emptyMap(),
    ): ResponseEntity<Void> =
        client
            .get()
            .uri(endpoint)
            .headers { header ->
                headers.forEach { (k, v) -> header.set(k, v) }
            }.accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .toBodilessEntity()
}
