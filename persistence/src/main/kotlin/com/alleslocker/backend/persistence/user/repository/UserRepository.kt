package com.alleslocker.backend.persistence.user.repository

import com.alleslocker.backend.persistence.user.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface UserRepository :
    JpaRepository<UserEntity, String>,
    JpaSpecificationExecutor<UserEntity> {
    fun findByUsername(username: String): UserEntity?
}
