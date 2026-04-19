package com.alleslocker.backend.domain.role

data class Role(
    val id: RoleId,
    val name: RoleName,
    val description: RoleDescription,
    val color: RoleColor
)
