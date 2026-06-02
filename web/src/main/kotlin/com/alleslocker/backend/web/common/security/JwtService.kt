package com.alleslocker.backend.web.common.security

import com.alleslocker.backend.application.common.Logger
import com.alleslocker.backend.web.common.config.JwtProperties
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.util.Date

@Component
class JwtService(
    private val jwtProperties: JwtProperties,
    private val logger: Logger,
) {
    private val key by lazy {
        Keys.hmacShaKeyFor(jwtProperties.secret.toByteArray())
    }

    fun generateToken(userId: String): String {
        val now = System.currentTimeMillis()

        return Jwts
            .builder()
            .setSubject(userId)
            .setIssuedAt(Date(now))
            .setExpiration(Date(now + jwtProperties.expiration))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }

    fun validate(token: String) {
        try {
            Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()

            logger.debug("JWT VALIDATED OK")
        } catch (ex: Exception) {
            logger.error("JWT ERROR: ${ex::class.simpleName} -> ${ex.message}", ex)
            throw ex
        }
    }

    fun extractUserId(token: String): String =
        Jwts
            .parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body
            .subject
}
