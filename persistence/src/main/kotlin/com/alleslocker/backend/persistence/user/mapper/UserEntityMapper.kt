package com.alleslocker.backend.persistence.user.mapper

import com.alleslocker.backend.domain.user.PasswordHash
import com.alleslocker.backend.domain.user.User
import com.alleslocker.backend.domain.user.UserEmail
import com.alleslocker.backend.domain.user.UserFirstname
import com.alleslocker.backend.domain.user.UserId
import com.alleslocker.backend.domain.user.UserLastname
import com.alleslocker.backend.domain.user.Username
import com.alleslocker.backend.persistence.user.entity.UserEntity

fun UserEntity.toDomain(): User =
    User(
        id = UserId(this.id),
        role = this.role,
        firstname = UserFirstname(this.firstname),
        lastname = UserLastname(this.lastname),
        email = UserEmail(this.email),
        username = Username(this.username),
        passwordHash = PasswordHash(this.passwordHash),
        isActive = this.isActive,
    )

fun User.toEntity(existing: UserEntity? = null): UserEntity {
    val entity = existing ?: UserEntity()

    entity.id = this.id.value
    entity.role = this.role
    entity.firstname = this.firstname.value
    entity.lastname = this.lastname.value
    entity.email = this.email.value
    entity.username = this.username.value
    entity.passwordHash = this.passwordHash.value
    entity.isActive = this.isActive

    return entity
}
