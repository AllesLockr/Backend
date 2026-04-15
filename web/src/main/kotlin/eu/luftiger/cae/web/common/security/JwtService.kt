package eu.luftiger.cae.web.common.security

import eu.luftiger.cae.web.common.config.JwtProperties
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.util.Date

@Component
class JwtService(
    private val jwtProperties: JwtProperties
) {
    private val key by lazy {
        Keys.hmacShaKeyFor(jwtProperties.secret.toByteArray())
    }

    fun generateToken(userId: String): String {
        val now = System.currentTimeMillis()

        return Jwts.builder()
            .setSubject(userId)
            .setIssuedAt(Date(now))
            .setExpiration(Date(now + jwtProperties.expiration))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }

    fun validate(token: String) {
        try {
            val parser = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()

            val claims = parser.parseClaimsJws(token)
            println("JWT VALIDATED OK. Claims: " + claims.body)

        } catch (ex: Exception) {
            println("JWT ERROR: ${ex::class.simpleName} -> ${ex.message}")
            throw ex
        }
    }

    fun extractUserId(token: String): String =
        Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body
            .subject
}