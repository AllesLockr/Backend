package com.alleslocker.backend.domain.api

sealed class ApiAuthentication {
    class ApiKey(val value: String) : ApiAuthentication()
    class BaseAuth(val username: ApiUsername, val password: ApiPassword) : ApiAuthentication()
}