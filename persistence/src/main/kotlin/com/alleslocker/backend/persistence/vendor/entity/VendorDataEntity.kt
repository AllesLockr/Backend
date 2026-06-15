package com.alleslocker.backend.persistence.vendor.entity

import com.alleslocker.backend.persistence.converter.CryptionConverter
import com.alleslocker.backend.persistence.shared.entity.MetadataEntryEntity
import jakarta.persistence.CollectionTable
import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.Table
import java.time.Instant

@Entity
@Table(name = "vendor_data")
open class VendorDataEntity {
    @Id
    @Column(name = "id", nullable = false, unique = true)
    open lateinit var id: String

    @Column(name = "for_api", nullable = false, unique = true)
    open lateinit var forApi: String

    @Column(name = "baseUrl", nullable = false, unique = true)
    open lateinit var baseUrl: String

    @Column(name = "vendor_connection_state", nullable = false)
    open lateinit var vendorConnectionState: String

    @Column(name = "last_checked", nullable = false)
    open lateinit var lastChecked: Instant

    @Convert(converter = CryptionConverter::class)
    @Column(name = "api_key", nullable = true)
    open var apiKey: String? = null

    @Column(name = "api_username", nullable = true)
    open var apiUsername: String? = null

    @Convert(converter = CryptionConverter::class)
    @Column(name = "api_password", nullable = true)
    open var apiPassword: String? = null

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "vendor_data_metadata", joinColumns = [JoinColumn(name = "vendor_data_id")])
    open var metadata: MutableSet<MetadataEntryEntity> = mutableSetOf()
}
