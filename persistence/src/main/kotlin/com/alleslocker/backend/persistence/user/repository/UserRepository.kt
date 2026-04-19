package com.alleslocker.backend.persistence.user.repository

import com.alleslocker.backend.persistence.user.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<UserEntity, String> {
    fun findByUsername(username: String): UserEntity?
}