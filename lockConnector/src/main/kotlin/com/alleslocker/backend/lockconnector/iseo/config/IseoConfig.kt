package com.alleslocker.backend.lockconnector.iseo.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "lock-connector-iseo")
// TODO: Delete / Move baseURl, user, pw to db read
class IseoConfig {
    var baseUrl: String = ""
    var username: String = ""
    var password: String = ""
    var clientId: String = "client"
    var clientSecret: String = ""
}