package com.alleslocker.backend.application.common.service

class PasswordGeneratorService {
    fun generate(length: Int = 8): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+"
        return (1..length).map { chars.random() }.joinToString("")
    }
}
