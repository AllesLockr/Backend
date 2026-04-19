package com.alleslocker.backend.application.common.gateway

interface ReadGateway<T, ID> : Gateway {
    fun findById(id: ID): T?
    fun exists(id: ID): Boolean
}