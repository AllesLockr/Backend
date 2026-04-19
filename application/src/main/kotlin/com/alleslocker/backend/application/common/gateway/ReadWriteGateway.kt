package com.alleslocker.backend.application.common.gateway

interface ReadWriteGateway<T, ID> : ReadGateway<T, ID> {
    fun save(entity: T): T
    fun deleteById(id: ID)
}