package com.alleslocker.backend.persistence.person.entity

import com.alleslocker.backend.domain.vendor.AvailableVendors
import jakarta.persistence.*

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
}
