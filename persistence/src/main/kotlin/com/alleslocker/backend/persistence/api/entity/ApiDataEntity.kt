package com.alleslocker.backend.persistence.api.entity

import com.alleslocker.backend.persistence.converter.CryptionConverter
import jakarta.persistence.*

@Entity
@Table(name = "api_data")
open class ApiDataEntity {
    @Id
    @Column(name = "id", nullable = false, unique = true)
    open lateinit var id: String

    @Column(name = "for_api", nullable = false, unique = true)
    open lateinit var forApi: String

    @Column(name = "baseUrl", nullable = false, unique = true)
    open lateinit var baseUrl: String

    @Convert(converter = CryptionConverter::class)
    @Column(name = "api_key", nullable = true)
    open var apiKey: String? = null

    @Column(name = "api_username", nullable = true)
    open var apiUsername: String? = null

    @Convert(converter = CryptionConverter::class)
    @Column(name = "api_password", nullable = true)
    open var apiPassword: String? = null
}
