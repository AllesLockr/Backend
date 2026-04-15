package eu.luftiger.cae.application.user.gateway

import eu.luftiger.cae.application.common.gateway.ReadWriteGateway
import eu.luftiger.cae.domain.user.User
import eu.luftiger.cae.domain.user.UserId

interface UserGateway : ReadWriteGateway<User, UserId> {
    fun findByUsername(username: String): User?
}