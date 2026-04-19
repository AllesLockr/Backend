package com.alleslocker.backend.application.common.gateway

interface ReadGateway<T, ID> : com.alleslocker.backend.application.common.gateway.Gateway {
    fun findById(id: ID): T?
    fun exists(id: ID): Boolean
}