package com.alleslocker.backend.domain.user

data class User(
    val id: UserId,
    val firstname: UserFirstname,
    val lastname: UserLastname,
    val username: Username,
    val email: UserEmail,
    val passwordHash: PasswordHash,
    val isActive: Boolean,
)
