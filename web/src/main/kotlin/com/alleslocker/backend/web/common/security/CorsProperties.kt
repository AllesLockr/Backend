package com.alleslocker.backend.web.common.security

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "app.cors")
open class CorsProperties {
    var allowedOrigins: List<String> = mutableListOf()
}