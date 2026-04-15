package eu.luftiger.cae.web.common.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "security.jwt")
class JwtProperties {
    lateinit var secret: String
    var expiration: Long = 0
}