package com.alleslocker.backend.bootstrap

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.architecture.KoArchitectureCreator.assertArchitecture
import com.lemonappdev.konsist.api.architecture.Layer
import com.lemonappdev.konsist.api.ext.list.imports
import com.lemonappdev.konsist.api.ext.list.withPackage
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldBeEmpty

class CleanArchitectureTest :
    FreeSpec({

        val scope = Konsist.scopeFromProduction()

        "Clean Architecture Layers" - {
            "should have correct dependencies" {
                val domain = Layer("Domain", "..domain..")
                val application = Layer("Application", "..application..")
                val web = Layer("Web", "..web..")
                val persistence = Layer("Persistence", "..persistence..")

                scope.assertArchitecture {
                    domain.dependsOnNothing()
                    application.dependsOn(domain)
                    application.doesNotDependOn(persistence, web)
                    persistence.dependsOn(application, domain)
                    persistence.doesNotDependOn(web)
                    web.dependsOn(application)
                    web.doesNotDependOn(persistence, domain)
                }
            }
        }

        "External Package Restrictions" - {

            val layerRestrictions =
                mapOf(
                    "domain" to
                        listOf(
                            "com.alleslocker.backend.domain",
                            "java.util",
                            "java.time",
                            "java.net",
                        ),
                    "application" to
                        listOf(
                            "com.alleslocker.backend.application",
                            "com.alleslocker.backend.domain",
                            "java.util",
                            "java.time",
                            "java.net",
                            "java.security",
                            "kotlin.reflect",
                            "kotlin.math",
                        ),
                    "persistence" to
                        listOf(
                            "com.alleslocker.backend.persistence",
                            "com.alleslocker.backend.domain",
                            "com.alleslocker.backend.application",
                            "kotlin.reflect",
                            "kotlin.properties",
                            "jakarta.persistence",
                            "org.springframework",
                            "java.time",
                            "java.net",
                            "java.nio",
                            "java.security",
                            "java.util",
                            "org.hibernate",
                            "javax.crypto",
                        ),
                    "web" to
                        listOf(
                            "com.alleslocker.backend.web",
                            "com.alleslocker.backend.application",
                            "kotlin.reflect",
                            "jakarta.servlet",
                            "org.springframework",
                            "java.io",
                            "io.jsonwebtoken",
                            "java.util",
                            "org.apache.coyote",
                            "java.time",
                            "io.swagger.v3.oas",
                        ),
                )

            layerRestrictions.forEach { (layerName, allowedImports) ->
                "$layerName layer should not depend on unauthorized external packages" {
                    scope.files
                        .withPackage("com.alleslocker.backend.$layerName..")
                        .imports
                        .filter { imp ->
                            allowedImports.none { allowed -> imp.name.startsWith(allowed) }
                        }.shouldBeEmpty()
                }
            }
        }
    })
