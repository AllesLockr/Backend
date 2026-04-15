package eu.luftiger.cae.bootstrap

import eu.luftiger.cae.web.common.config.JwtProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@ComponentScan(basePackages = ["eu.luftiger.cae.bootstrap", "eu.luftiger.cae.web", "eu.luftiger.cae.persistence"])
@EnableJpaRepositories(basePackages = ["eu.luftiger.cae.persistence"])
@EntityScan(basePackages = ["eu.luftiger.cae.persistence"])
@EnableConfigurationProperties(JwtProperties::class)
open class CAEApplication

fun main(args: Array<String>) {
    runApplication<CAEApplication>(*args)
}
