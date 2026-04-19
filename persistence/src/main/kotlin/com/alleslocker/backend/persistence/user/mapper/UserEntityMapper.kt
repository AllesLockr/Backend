package com.alleslocker.backend.persistence.user.mapper

import com.alleslocker.backend.domain.user.PasswordHash
import com.alleslocker.backend.domain.user.User
import com.alleslocker.backend.domain.user.UserId
import com.alleslocker.backend.domain.user.Username
import com.alleslocker.backend.persistence.user.entity.UserEntity

fun UserEntity.toDomain(): User = User(
    id = UserId(this.id),
    username = Username(this.username),
    passwordHash = PasswordHash(this.passwordHash)
)

fun User.toEntity(existing: UserEntity? = null): UserEntity {
    val entity = existing ?: UserEntity()

    entity.id = this.id.value
    entity.username = this.username.value
    entity.passwordHash = this.passwordHash.value

    return entity
}