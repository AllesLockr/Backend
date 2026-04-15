package eu.luftiger.cae.domain.user

data class User(
    val id: UserId,
    val username: Username,
    val passwordHash: PasswordHash
)