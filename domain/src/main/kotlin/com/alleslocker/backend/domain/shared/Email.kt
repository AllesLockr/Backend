package com.alleslocker.backend.domain.shared

object Email {
    private val EMAIL_REGEX = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")

    fun isValid(value: String): Boolean = EMAIL_REGEX.matches(value)
}
