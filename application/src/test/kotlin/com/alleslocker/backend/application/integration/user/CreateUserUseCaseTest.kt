package com.alleslocker.backend.application.integration.user

import com.alleslocker.backend.application.common.ErrorResponse
import com.alleslocker.backend.application.integration.config.TestPresenter
import com.alleslocker.backend.application.integration.config.createUserTestContext
import com.alleslocker.backend.application.user.dto.request.CreateUserRequestDto
import com.alleslocker.backend.application.user.dto.response.CreateUserResponseDto
import com.alleslocker.backend.application.user.usecase.CreateUserUseCase
import com.alleslocker.backend.domain.user.PasswordHash
import com.alleslocker.backend.domain.user.User
import com.alleslocker.backend.domain.user.UserEmail
import com.alleslocker.backend.domain.user.UserFirstname
import com.alleslocker.backend.domain.user.UserId
import com.alleslocker.backend.domain.user.UserLastname
import com.alleslocker.backend.domain.user.UserRole
import com.alleslocker.backend.domain.user.Username
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.comparables.shouldBeGreaterThanOrEqualTo
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.types.shouldBeInstanceOf

class CreateUserUseCaseTest :
    FreeSpec({
        val ctx = createUserTestContext()
        val useCase: CreateUserUseCase = ctx.useCaseFactory.make(CreateUserUseCase::class)

        lateinit var adminId: String

        beforeEach {
            ctx.userGateway.deleteAll()
            ctx.logger.clear()
            val admin =
                ctx.userGateway.save(
                    User(
                        id = UserId.generate(),
                        role = UserRole.ADMIN,
                        firstname = UserFirstname("Admin"),
                        lastname = UserLastname("User"),
                        username = Username("admin"),
                        email = UserEmail("admin@test.de"),
                        passwordHash = PasswordHash(ctx.passwordHasher.hash("admin123")),
                        isActive = true,
                        mustChangePassword = false,
                    ),
                )
            adminId = admin.id.value
        }

        "should create a user as admin" {
            val presenter = TestPresenter<CreateUserResponseDto>()

            useCase.execute(
                CreateUserRequestDto(
                    requestorId = adminId,
                    firstname = "Max",
                    lastname = "Mustermann",
                    username = "mmuster",
                    email = "max@test.de",
                ),
                presenter,
            )

            presenter.error shouldBe null
            presenter.response shouldNotBe null
            presenter.response!!.id shouldNotBe ""
            presenter.response!!.password.length shouldBeGreaterThan 0

            val savedUser = ctx.userGateway.findById(UserId(presenter.response!!.id))
            savedUser shouldNotBe null
            savedUser!!.mustChangePassword shouldBe true
            savedUser.isActive shouldBe true
            savedUser.username.value shouldBe "mmuster"
            savedUser.email.value shouldBe "max@test.de"
            ctx.passwordHasher.verify(presenter.response!!.password, savedUser.passwordHash.value) shouldBe true

            ctx.logger.auditLogs.size shouldBe 1
            ctx.logger.auditLogs[0]
                .message.value shouldContain "mmuster"
        }

        "should reject duplicate username" {
            ctx.userGateway.save(
                User(
                    id = UserId.generate(),
                    role = UserRole.USER,
                    firstname = UserFirstname("Existing"),
                    lastname = UserLastname("User"),
                    username = Username("mmuster"),
                    email = UserEmail("other@test.de"),
                    passwordHash = PasswordHash(ctx.passwordHasher.hash("pass123")),
                    isActive = true,
                    mustChangePassword = false,
                ),
            )

            val presenter = TestPresenter<CreateUserResponseDto>()
            useCase.execute(
                CreateUserRequestDto(
                    requestorId = adminId,
                    firstname = "Max",
                    lastname = "Mustermann",
                    username = "mmuster",
                    email = "max@test.de",
                ),
                presenter,
            )

            presenter.response shouldBe null
            presenter.error shouldNotBe null
            presenter.error!!.shouldBeInstanceOf<ErrorResponse.AlreadyExists>()
            presenter.error!!.message shouldContain "username"
        }

        "should reject duplicate email" {
            ctx.userGateway.save(
                User(
                    id = UserId.generate(),
                    role = UserRole.USER,
                    firstname = UserFirstname("Existing"),
                    lastname = UserLastname("User"),
                    username = Username("existing"),
                    email = UserEmail("max@test.de"),
                    passwordHash = PasswordHash(ctx.passwordHasher.hash("pass123")),
                    isActive = true,
                    mustChangePassword = false,
                ),
            )

            val presenter = TestPresenter<CreateUserResponseDto>()
            useCase.execute(
                CreateUserRequestDto(
                    requestorId = adminId,
                    firstname = "Max",
                    lastname = "Mustermann",
                    username = "mmuster",
                    email = "max@test.de",
                ),
                presenter,
            )

            presenter.response shouldBe null
            presenter.error shouldNotBe null
            presenter.error!!.shouldBeInstanceOf<ErrorResponse.AlreadyExists>()
            presenter.error!!.message shouldContain "email"
        }

        "should reject non-admin requestor" {
            val regularUser =
                ctx.userGateway.save(
                    User(
                        id = UserId.generate(),
                        role = UserRole.USER,
                        firstname = UserFirstname("Regular"),
                        lastname = UserLastname("User"),
                        username = Username("regular"),
                        email = UserEmail("regular@test.de"),
                        passwordHash = PasswordHash(ctx.passwordHasher.hash("pass123")),
                        isActive = true,
                        mustChangePassword = false,
                    ),
                )

            val presenter = TestPresenter<CreateUserResponseDto>()
            useCase.execute(
                CreateUserRequestDto(
                    requestorId = regularUser.id.value,
                    firstname = "Max",
                    lastname = "Mustermann",
                    username = "mmuster",
                    email = "max@test.de",
                ),
                presenter,
            )

            presenter.response shouldBe null
            presenter.error shouldNotBe null
            presenter.error!!.shouldBeInstanceOf<ErrorResponse.Unauthorized>()
        }

        "should reject requestor that does not exist" {
            val presenter = TestPresenter<CreateUserResponseDto>()
            useCase.execute(
                CreateUserRequestDto(
                    requestorId = "non-existent-id",
                    firstname = "Max",
                    lastname = "Mustermann",
                    username = "mmuster",
                    email = "max@test.de",
                ),
                presenter,
            )

            presenter.response shouldBe null
            presenter.error shouldNotBe null
            presenter.error!!.shouldBeInstanceOf<ErrorResponse.NotFound>()
        }

        "should reject invalid firstname (blank)" {
            val presenter = TestPresenter<CreateUserResponseDto>()
            useCase.execute(
                CreateUserRequestDto(
                    requestorId = adminId,
                    firstname = "",
                    lastname = "Mustermann",
                    username = "mmuster",
                    email = "max@test.de",
                ),
                presenter,
            )

            presenter.response shouldBe null
            presenter.error shouldNotBe null
            presenter.error!!.shouldBeInstanceOf<ErrorResponse.BadRequest>()
        }

        "should reject invalid email format" {
            val presenter = TestPresenter<CreateUserResponseDto>()
            useCase.execute(
                CreateUserRequestDto(
                    requestorId = adminId,
                    firstname = "Max",
                    lastname = "Mustermann",
                    username = "mmuster",
                    email = "invalid-email",
                ),
                presenter,
            )

            presenter.response shouldBe null
            presenter.error shouldNotBe null
            presenter.error!!.shouldBeInstanceOf<ErrorResponse.BadRequest>()
        }

        "should reject username shorter than 3 characters" {
            val presenter = TestPresenter<CreateUserResponseDto>()
            useCase.execute(
                CreateUserRequestDto(
                    requestorId = adminId,
                    firstname = "Max",
                    lastname = "Mustermann",
                    username = "ab",
                    email = "max@test.de",
                ),
                presenter,
            )

            presenter.response shouldBe null
            presenter.error shouldNotBe null
            presenter.error!!.shouldBeInstanceOf<ErrorResponse.BadRequest>()
        }

        "should reject username with special characters" {
            val presenter = TestPresenter<CreateUserResponseDto>()
            useCase.execute(
                CreateUserRequestDto(
                    requestorId = adminId,
                    firstname = "Max",
                    lastname = "Mustermann",
                    username = "user name!",
                    email = "max@test.de",
                ),
                presenter,
            )

            presenter.response shouldBe null
            presenter.error shouldNotBe null
            presenter.error!!.shouldBeInstanceOf<ErrorResponse.BadRequest>()
        }

        "generated password should be at least 8 characters" {
            val presenter = TestPresenter<CreateUserResponseDto>()
            useCase.execute(
                CreateUserRequestDto(
                    requestorId = adminId,
                    firstname = "Max",
                    lastname = "Mustermann",
                    username = "mmuster",
                    email = "max@test.de",
                ),
                presenter,
            )

            presenter.error shouldBe null
            presenter.response!!.password.length shouldBeGreaterThanOrEqualTo 8
        }
    })
