package eu.luftiger.cae.persistence.user.mapper

import eu.luftiger.cae.domain.user.PasswordHash
import eu.luftiger.cae.domain.user.User
import eu.luftiger.cae.domain.user.UserId
import eu.luftiger.cae.domain.user.Username
import eu.luftiger.cae.persistence.user.entity.UserEntity

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