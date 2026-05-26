package com.alleslocker.backend.domain.api

import java.net.URI

data class ApiData(
    val id: ApiId,
    val forApi: AvailableApis,
    val baseUrl: URI,
    val apiAuthentication: ApiAuthentication,
) {
    init {
        require(baseUrl.toString().isNotEmpty()) { "baseUrl must not be empty" }
    }
}