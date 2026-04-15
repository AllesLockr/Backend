package eu.luftiger.cae.web.common.security

import eu.luftiger.cae.application.security.PasswordHasher
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component

@Component
class BcryptPasswordHasher : PasswordHasher {
    private val encoder = BCryptPasswordEncoder()

    override fun hash(raw: String): String = encoder.encode(raw)
    override fun verify(raw: String, hash: String): Boolean = encoder.matches(raw, hash)
}