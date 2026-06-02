package com.alleslocker.backend.persistence.person.entity

import com.alleslocker.backend.domain.vendor.AvailableVendors
import com.alleslocker.backend.persistence.shared.entity.MetadataEntryEntity
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

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "person_external_ids", joinColumns = [JoinColumn(name = "person_id")])
    @MapKeyEnumerated(EnumType.STRING)
    @MapKeyColumn(name = "api")
    @Column(name = "external_id", nullable = false)
    open var externalIds: MutableMap<AvailableVendors, String> = mutableMapOf()

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "person_metadata", joinColumns = [JoinColumn(name = "person_id")])
    open var metadata: MutableSet<MetadataEntryEntity> = mutableSetOf()
}
