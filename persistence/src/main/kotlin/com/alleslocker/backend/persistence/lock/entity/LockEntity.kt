package com.alleslocker.backend.persistence.lock.entity

import com.alleslocker.backend.domain.api.AvailableApis
import com.alleslocker.backend.persistence.shared.entity.MetadataEntity
import jakarta.persistence.CollectionTable
import jakarta.persistence.Column
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.Table

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

    @Enumerated(EnumType.STRING)
    @Column(name = "external_api", nullable = true)
    open var externalApi: AvailableApis? = null

    @Column(name = "external_id", nullable = true)
    open var externalId: String? = null

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "lock_metadata", joinColumns = [JoinColumn(name = "lock_id")])
    open var metadata: MutableSet<MetadataEntity> = mutableSetOf()
}
