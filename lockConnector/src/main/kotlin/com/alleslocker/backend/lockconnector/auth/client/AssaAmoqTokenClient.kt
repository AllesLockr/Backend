package com.alleslocker.backend.lockconnector.auth.client

import com.alleslocker.backend.lockconnector.auth.common.TokenClient
import com.alleslocker.backend.lockconnector.auth.common.TokenResponse

class AssaAmoqTokenClient : TokenClient {
    override fun getToken(): TokenResponse =
        throw IllegalStateException("This shouldn't happen. AssaAmok API has no authentication configured!")
}
