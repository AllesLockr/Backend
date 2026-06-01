package com.alleslocker.backend.lockconnector.client

interface TokenClient {
    fun getToken(): TokenResponse
}

