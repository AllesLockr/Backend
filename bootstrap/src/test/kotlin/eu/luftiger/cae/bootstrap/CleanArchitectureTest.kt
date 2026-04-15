package eu.luftiger.cae.bootstrap

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.architecture.KoArchitectureCreator.assertArchitecture
import com.lemonappdev.konsist.api.architecture.Layer
import com.lemonappdev.konsist.api.ext.list.imports
import com.lemonappdev.konsist.api.ext.list.withPackage
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldBeEmpty

class CleanArchitectureTest : FreeSpec({

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

        val layerRestrictions = mapOf(
            "domain" to listOf(
                "eu.luftiger.cae.domain",
                "java.util"
            ),
            "application" to listOf(
                "eu.luftiger.cae.application",
                "eu.luftiger.cae.domain",
                "java.util",
                "kotlin.reflect"
            ),
            "persistence" to listOf(
                "eu.luftiger.cae.persistence",
                "eu.luftiger.cae.domain",
                "eu.luftiger.cae.application",
                "kotlin.reflect",
                "jakarta.persistence",
                "org.springframework"
            ),
            "web" to listOf(
                "eu.luftiger.cae.web",
                "eu.luftiger.cae.application",
                "kotlin.reflect",
                "jakarta.servlet",
                "org.springframework",
                "java.io",
                "io.jsonwebtoken",
                "java.util",
                "org.apache.coyote"
            )
        )

        layerRestrictions.forEach { (layerName, allowedImports) ->
            "$layerName layer should not depend on unauthorized external packages" {
                scope.files
                    .withPackage("eu.luftiger.cae.$layerName..")
                    .imports
                    .filter { imp ->
                        allowedImports.none { allowed -> imp.name.startsWith(allowed) }
                    }
                    .shouldBeEmpty()
            }
        }
    }
})