package com.alleslocker.backend.lockconnector.client

data class TokenResponse(
    val accessToken: String,
    val expiresIn: Long,
)