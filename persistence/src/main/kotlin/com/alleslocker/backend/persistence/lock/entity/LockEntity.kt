package com.alleslocker.backend.persistence.lock.entity

import com.alleslocker.backend.domain.api.AvailableApis
import jakarta.persistence.CollectionTable
import jakarta.persistence.Column
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.MapKeyColumn
import jakarta.persistence.MapKeyEnumerated
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

    @Column(name = "tag_id", nullable = true, unique = false)
    open var tagId: Long? = null

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "lock_external_ids", joinColumns = [JoinColumn(name = "lock_id")])
    @MapKeyEnumerated(EnumType.STRING)
    @MapKeyColumn(name = "api")
    @Column(name = "external_id", nullable = false)
    open var externalIds: MutableMap<AvailableApis, String> = mutableMapOf()
}