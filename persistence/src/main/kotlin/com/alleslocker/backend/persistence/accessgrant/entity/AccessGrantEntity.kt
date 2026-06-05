package com.alleslocker.backend.persistence.accessgrant.entity

import com.alleslocker.backend.domain.accessgrant.AccessOperation
import com.alleslocker.backend.domain.vendor.AvailableVendors
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant

@Entity
@Table(name = "access_grants")
open class AccessGrantEntity {
    @Id
    @Column(name = "id", nullable = false, unique = true)
    open lateinit var id: String

    @Column(name = "person_id", nullable = false)
    open lateinit var personId: String

    @Column(name = "lock_id", nullable = false)
    open lateinit var lockId: String

    @Column(name = "start_at", nullable = false)
    open lateinit var startAt: Instant

    @Column(name = "end_at", nullable = false)
    open lateinit var endAt: Instant

    @Enumerated(EnumType.STRING)
    @Column(name = "operation", nullable = false)
    open lateinit var operation: AccessOperation

    @Enumerated(EnumType.STRING)
    @Column(name = "external_api", nullable = true)
    open var externalApi: AvailableVendors? = null

    @Column(name = "external_id", nullable = true)
    open var externalId: String? = null
}
