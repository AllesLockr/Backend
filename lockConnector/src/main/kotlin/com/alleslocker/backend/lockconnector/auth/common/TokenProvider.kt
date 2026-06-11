package com.alleslocker.backend.lockconnector.auth.common

import java.time.Instant

class TokenProvider(
    private val tokenClient: TokenClient,
) {
    private data class CachedToken(
        val token: String,
        val expiresAt: Instant,
    )

    private var cachedToken: CachedToken? = null

    @Synchronized
    fun getValidToken(): String {
        val now = Instant.now()
        cachedToken?.let {
            if (now.isBefore(it.expiresAt.minusSeconds(5))) {
                return it.token
            }
        }
        val response = tokenClient.getToken()
        val newToken =
            CachedToken(
                token = response.accessToken,
                expiresAt = now.plusSeconds(response.expiresIn),
            )
        cachedToken = newToken
        return newToken.token
    }
}
