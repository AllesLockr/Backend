package com.alleslocker.backend.application.common.service

import java.security.SecureRandom

class PasswordGeneratorService {
    private val secureRandom = SecureRandom()

    fun generate(length: Int = 8): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+"
        return (1..length).map { chars[secureRandom.nextInt(chars.length)] }.joinToString("")
    }
}
