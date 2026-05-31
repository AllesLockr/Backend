package com.alleslocker.backend.lockconnector.iseo.client

import org.springframework.stereotype.Component
import java.time.Instant

@Component
class IseoTokenProvider(
    private val tokenClient: IseoTokenClient,
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
