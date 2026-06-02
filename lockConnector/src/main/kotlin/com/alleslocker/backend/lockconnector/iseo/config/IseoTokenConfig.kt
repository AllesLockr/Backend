package com.alleslocker.backend.lockconnector.iseo.config

import com.alleslocker.backend.domain.vendor.AvailableVendors
import com.alleslocker.backend.lockconnector.client.TokenProvider
import com.alleslocker.backend.lockconnector.iseo.client.IseoOAuthTokenClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class IseoTokenConfig {
    @Bean("iseoAdminTokenProvider")
    open fun iseoAdminTokenProvider(configProvider: ConfigProvider) =
        TokenProvider(IseoOAuthTokenClient(configProvider, AvailableVendors.ISEO))

    @Bean("iseoInstallerTokenProvider")
    open fun iseoInstallerTokenProvider(configProvider: ConfigProvider) =
        TokenProvider(IseoOAuthTokenClient(configProvider, AvailableVendors.ISEO_INSTALLER))
}
