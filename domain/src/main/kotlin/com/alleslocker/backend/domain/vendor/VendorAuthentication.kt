package com.alleslocker.backend.domain.vendor

sealed class VendorAuthentication {
    class ApiKey(
        val value: String,
    ) : VendorAuthentication()

    class BaseAuth(
        val username: ApiUsername,
        val password: ApiPassword,
    ) : VendorAuthentication()
}
