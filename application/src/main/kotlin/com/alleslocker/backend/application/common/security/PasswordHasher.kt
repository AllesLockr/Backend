package com.alleslocker.backend.application.common.security

interface PasswordHasher {
    fun hash(raw: String): String
    fun verify(raw: String, hash: String): Boolean
}