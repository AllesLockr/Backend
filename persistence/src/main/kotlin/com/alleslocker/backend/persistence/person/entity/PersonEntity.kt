package com.alleslocker.backend.persistence.person.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "person")
open class PersonEntity {

    @Id
    @Column(name = "id", nullable = false, unique = true)
    open lateinit var id: String

    @Column(name = "firstname", nullable = false, unique = false)
    open lateinit var firstname: String

    @Column(name = "lastname", nullable = false, unique = false)
    open lateinit var lastname: String

    @Column(name = "email", nullable = false, unique = true)
    open lateinit var email: String
}