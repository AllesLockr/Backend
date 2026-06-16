# AllesLocker Backend — Agent Guide

## Build & verify commands

```sh
./gradlew ktlintCheck        # lint only (CI runs this)
./gradlew ktlintFormat       # auto-fix style
./gradlew check              # ktlintCheck + tests (1 exists: CleanArchitectureTest)
./gradlew bootJar            # build Docker-ready JAR
./gradlew :module:test       # run single module's tests
```

`ktlintCheck` is part of `check`; `ktlintFormat` auto-fixes style issues.

## Architecture

6 modules, Clean Architecture enforced by Konsist arch test (`bootstrap/src/test/.../CleanArchitectureTest.kt`):

```
domain ──→ application ──→ persistence, web, lockConnector ──→ bootstrap
(nothing)   (domain only)   (domain+app)                        (all: composition root)
```

- `domain`: pure Kotlin value objects, no framework annotations
- `application`: use cases + gateway/adapter interfaces, **no Spring beans** (manual DI)
- `persistence`: JPA entities + gateway adapter implementations (`@Component`)
- `web`: REST controllers + presenters + JWT security
- `lockConnector`: vendor REST clients (ISEO, Assa Abloy AMOQ) with OAuth2
- `bootstrap`: entry point, Spring config, DI wiring

Package base: `com.alleslocker.backend.<module>`.

### Key patterns (non-obvious)

- **Presenter pattern**: Controllers never return values. They create a Presenter (`OutputBoundary`), pass it to `useCase.execute(request, presenter)`, and the presenter writes directly to `HttpServletResponse`.
- **Manual DI in application layer**: Use cases are instantiated inside `UseCaseFactoryImpl`. They receive gateways/adapters through `GatewayFactory` and `AdapterFactory` service locators, not Spring injection.
- **JPA entities** need `open` modifier (Kotlin noarg plugin handles this at compile time).
- **ComponentScan** only covers `bootstrap`, `web`, `persistence`, `lockConnector` — NOT `application` or `domain` (they have no Spring beans).
- **`@EnableJpaRepositories`** and **`@EntityScan`** scoped to `com.alleslocker.backend.persistence`.

## Local dev

```sh
docker compose up -d          # starts MariaDB 11 on :3306
./gradlew bootJar && java -jar bootstrap/build/libs/*.jar
```

- Default profile: `dev` (active in `application.yml`)
- Dev config (`application-dev.yml`): hardcoded credentials, `ddl-auto: update`, JWT secret
- `application-local.yml` is gitignored — override secrets locally
- `docker-compose.yml` is also gitignored; the committed copy is a template
- `app.security.master-key`: AES key for encrypting vendor API credentials at rest
- Scheduled task checks vendor connection every 5 min (`app.scheduling.vendor-connection-check: 300000`)

## CI

- **ci.yml**: `ktlintCheck` + `./gradlew build` on JDK 21 (Temurin), runs on every push/PR
- **deploy-demo.yml**: manual `workflow_dispatch`, builds Docker image, deploys via docker-compose on self-hosted runner

## Style & conventions

- `.editorconfig`: 4-space indent, LF, 120 max line width, star imports disabled
- `kotlin.code.style=official` in `gradle.properties`
- Conventional Commits (`CONTRIBUTING.md`): branch prefixes `feat/`, `fix/`, `docs/`, `refactor/`, `chore/`; commit format `type(scope): description` (imperative, ≤50 chars)

## Testing

- **1 test exists**: `CleanArchitectureTest` — uses **Kotest** (`FreeSpec`) + **Konsist**; enforces layer dependency rules
- Kotest 5.7.2 with JUnit5 runner, `kotest-extensions-spring` available
- H2 in-memory DB declared in version catalog (not used yet)
- All modules use `useJUnitPlatform()` automatically

## Docker

- Multi-stage: `eclipse-temurin:21-jdk-alpine` builder → `eclipse-temurin:21-jre-alpine` runtime
- JAR artifact: `bootstrap/build/libs/*.jar`
- Exposes `:8080`, runs as non-root `appuser`
