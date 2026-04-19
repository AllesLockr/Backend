package com.alleslocker.backend.web.common.security

import com.alleslocker.backend.application.common.security.PasswordHasher
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component

@Component
class BcryptPasswordHasher : PasswordHasher {
    private val encoder = BCryptPasswordEncoder()

    override fun hash(raw: String): String = encoder.encode(raw)
    override fun verify(raw: String, hash: String): Boolean = encoder.matches(raw, hash)
}