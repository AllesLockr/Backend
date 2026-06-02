package com.alleslocker.backend.application.lock.gateway

import com.alleslocker.backend.application.common.gateway.ReadWriteGateway
import com.alleslocker.backend.application.common.model.Page
import com.alleslocker.backend.domain.lock.Lock
import com.alleslocker.backend.domain.lock.LockId
import com.alleslocker.backend.domain.lock.LockSerialNumber

interface LockGateway : ReadWriteGateway<Lock, LockId> {
    fun getAllLocksPaged(
        page: Int = 0,
        size: Int = 10,
    ): Page<Lock>
    fun findBySerialNumber(serialNumber: LockSerialNumber): Lock?
    fun findAll(): List<Lock>
}
