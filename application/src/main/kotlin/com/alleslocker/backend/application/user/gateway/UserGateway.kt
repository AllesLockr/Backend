package com.alleslocker.backend.application.user.gateway

import com.alleslocker.backend.application.common.gateway.ReadWriteGateway
import com.alleslocker.backend.application.common.model.Page
import com.alleslocker.backend.application.user.dto.filter.UserFilterDto
import com.alleslocker.backend.domain.user.User
import com.alleslocker.backend.domain.user.UserId

interface UserGateway : ReadWriteGateway<User, UserId> {
    fun findByUsername(username: String): User?

    fun findByEmail(email: String): User?

    fun existsByEmail(email: String): Boolean

    fun existsByUsername(username: String): Boolean

    fun getAllUsersPaged(
        filter: UserFilterDto,
        page: Int = 0,
        size: Int = 10,
    ): Page<User>
}
