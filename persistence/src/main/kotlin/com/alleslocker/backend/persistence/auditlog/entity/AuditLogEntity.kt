package com.alleslocker.backend.persistence.auditlog.entity

import com.alleslocker.backend.persistence.user.entity.UserEntity
import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "audit_logs")
open class AuditLogEntity {
    @Id
    @Column(name = "id", nullable = false, unique = true)
    open lateinit var id: String

    @Column(name = "message", nullable = false, unique = false)
    open lateinit var message: String

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performed_by", nullable = false)
    open lateinit var performedBy: UserEntity

    @Column(name = "created_at", nullable = false, unique = false)
    open lateinit var createdAt: Instant
}