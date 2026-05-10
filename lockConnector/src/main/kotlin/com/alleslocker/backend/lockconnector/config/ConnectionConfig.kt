package com.alleslocker.backend.lockconnector.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties("lock-connector")
class ConnectionConfig {
    lateinit var baseUrl: String
    val person: PersonConfig = PersonConfig()

    class PersonConfig {
        val addPerson: EndpointConfig = EndpointConfig()
        val deletePerson: EndpointConfig = EndpointConfig()
    }
    class EndpointConfig {
        lateinit var endpoint: String
        val parameters: MutableMap<String, String> = mutableMapOf()
    }

}