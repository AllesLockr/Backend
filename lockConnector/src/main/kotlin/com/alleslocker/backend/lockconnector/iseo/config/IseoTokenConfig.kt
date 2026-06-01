package com.alleslocker.backend.lockconnector.iseo.config

import com.alleslocker.backend.domain.api.AvailableApis
import com.alleslocker.backend.lockconnector.client.TokenProvider
import com.alleslocker.backend.lockconnector.iseo.client.IseoOAuthTokenClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class IseoTokenConfig {
    @Bean("iseoAdminTokenProvider")
    open fun iseoAdminTokenProvider(configProvider: ConfigProvider) =
        TokenProvider(IseoOAuthTokenClient(configProvider, AvailableApis.ISEO))

    @Bean("iseoInstallerTokenProvider")
    open fun iseoInstallerTokenProvider(configProvider: ConfigProvider) =
        TokenProvider(IseoOAuthTokenClient(configProvider, AvailableApis.ISEO_INSTALLER))
}