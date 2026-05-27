package com.alleslocker.backend.lockconnector.iseo.client

import com.fasterxml.jackson.annotation.JsonProperty

data class IseoTokenResponse(
    @JsonProperty("access_token")
    val accessToken: String,
    @JsonProperty("token_type")
    val tokenType: String,
    @JsonProperty("refresh_token")
    val refreshToken: String?,
    @JsonProperty("expires_in")
    val expiresIn: Long,
)