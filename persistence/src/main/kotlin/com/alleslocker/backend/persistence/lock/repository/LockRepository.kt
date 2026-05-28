package com.alleslocker.backend.persistence.lock.repository

import com.alleslocker.backend.persistence.lock.entity.LockEntity
import org.springframework.data.jpa.repository.JpaRepository

interface LockRepository : JpaRepository<LockEntity, String>