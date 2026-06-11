package com.alleslocker.backend.lockconnector.auth.common

interface TokenClient {
    fun getToken(): TokenResponse
}
