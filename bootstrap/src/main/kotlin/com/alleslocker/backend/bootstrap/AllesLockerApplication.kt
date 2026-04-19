package com.alleslocker.backend.bootstrap

import com.alleslocker.backend.web.common.config.JwtProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@ComponentScan(basePackages = ["com.alleslocker.backend.bootstrap", "com.alleslocker.backend.web", "com.alleslocker.backend.persistence"])
@EnableJpaRepositories(basePackages = ["com.alleslocker.backend.persistence"])
@EntityScan(basePackages = ["com.alleslocker.backend.persistence"])
@EnableConfigurationProperties(JwtProperties::class)
open class AllesLockerApplication

fun main(args: Array<String>) {
    runApplication<AllesLockerApplication>(*args)
}
