package com.alleslocker.backend.persistence.lock.entity

import com.alleslocker.backend.domain.vendor.AvailableVendors
import jakarta.persistence.*

@Entity
@Table(name = "locks")
open class LockEntity {
    @Id
    @Column(name = "id", nullable = false, unique = true)
    open lateinit var id: String

    @Column(name = "name", nullable = false, unique = false)
    open lateinit var name: String

    @Column(name = "serial_number", nullable = false, unique = true)
    open lateinit var serialNumber: String

    @Column(name = "tag_id", nullable = true, unique = false)
    open var tagId: Long? = null

    @Enumerated(EnumType.STRING)
    @Column(name = "external_api", nullable = true)
    open var externalApi: AvailableVendors? = null

    @Column(name = "external_id", nullable = true)
    open var externalId: String? = null
}
