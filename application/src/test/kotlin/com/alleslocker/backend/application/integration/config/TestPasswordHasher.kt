package com.alleslocker.backend.application.integration.config

import com.alleslocker.backend.application.common.security.PasswordHasher
import java.security.MessageDigest

class TestPasswordHasher : PasswordHasher {
    private val digest = MessageDigest.getInstance("SHA-256")

    override fun hash(raw: String): String = digest.digest(raw.toByteArray()).joinToString("") { "%02x".format(it) }

    override fun verify(
        raw: String,
        hash: String,
    ): Boolean = hash(raw) == hash
}
