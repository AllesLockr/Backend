package eu.luftiger.cae.persistence.user.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "user")
open class UserEntity {

    @Id
    @Column(name = "id", nullable = false, unique = true)
    open lateinit var id: String

    @Column(name = "username", nullable = false, unique = true)
    open lateinit var username: String

    @Column(name = "password_hash", nullable = false)
    open lateinit var passwordHash: String
}