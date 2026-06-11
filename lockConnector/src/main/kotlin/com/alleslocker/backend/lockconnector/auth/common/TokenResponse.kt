package com.alleslocker.backend.lockconnector.auth.common

data class TokenResponse(
    val accessToken: String,
    val expiresIn: Long,
)
