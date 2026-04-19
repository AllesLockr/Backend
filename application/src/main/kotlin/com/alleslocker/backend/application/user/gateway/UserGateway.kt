package com.alleslocker.backend.application.user.gateway

import com.alleslocker.backend.application.common.gateway.ReadWriteGateway
import com.alleslocker.backend.domain.user.User
import com.alleslocker.backend.domain.user.UserId

interface UserGateway : ReadWriteGateway<User, UserId> {
    fun findByUsername(username: String): User?
}