package com.alleslocker.backend.application.common.gateway

interface ReadWriteGateway<T, ID> : com.alleslocker.backend.application.common.gateway.ReadGateway<T, ID> {
    fun save(entity: T): T
    fun deleteById(id: ID)
}