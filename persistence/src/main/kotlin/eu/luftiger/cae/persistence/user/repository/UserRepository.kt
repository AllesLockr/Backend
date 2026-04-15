package eu.luftiger.cae.persistence.user.repository

import eu.luftiger.cae.persistence.user.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<UserEntity, String> {
    fun findByUsername(username: String): UserEntity?
}