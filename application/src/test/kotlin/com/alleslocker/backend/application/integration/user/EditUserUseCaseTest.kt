package com.alleslocker.backend.application.integration.user

import com.alleslocker.backend.application.common.ErrorResponse
import com.alleslocker.backend.application.integration.config.TestPresenter
import com.alleslocker.backend.application.integration.config.createUserTestContext
import com.alleslocker.backend.application.user.dto.request.EditUserRequestDto
import com.alleslocker.backend.application.user.usecase.EditUserUseCase
import com.alleslocker.backend.domain.user.PasswordHash
import com.alleslocker.backend.domain.user.User
import com.alleslocker.backend.domain.user.UserEmail
import com.alleslocker.backend.domain.user.UserFirstname
import com.alleslocker.backend.domain.user.UserId
import com.alleslocker.backend.domain.user.UserLastname
import com.alleslocker.backend.domain.user.UserRole
import com.alleslocker.backend.domain.user.Username
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.types.shouldBeInstanceOf

class EditUserUseCaseTest :
    FreeSpec({
        val ctx = createUserTestContext()
        val useCase: EditUserUseCase = ctx.useCaseFactory.make(EditUserUseCase::class)

        lateinit var adminId: String
        lateinit var targetUserId: String

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

            val targetUser =
                ctx.userGateway.save(
                    User(
                        id = UserId.generate(),
                        role = UserRole.USER,
                        firstname = UserFirstname("Max"),
                        lastname = UserLastname("Mustermann"),
                        username = Username("mmuster"),
                        email = UserEmail("max@test.de"),
                        passwordHash = PasswordHash(ctx.passwordHasher.hash("pass123")),
                        isActive = true,
                        mustChangePassword = false,
                    ),
                )
            targetUserId = targetUser.id.value
        }

        "should update firstname" {
            val presenter = TestPresenter<Unit>()

            useCase.execute(
                EditUserRequestDto(
                    requestorId = adminId,
                    userId = targetUserId,
                    firstname = "Maximilian",
                ),
                presenter,
            )

            presenter.error shouldBe null
            val updatedUser = ctx.userGateway.findById(UserId(targetUserId))
            updatedUser!!.firstname.value shouldBe "Maximilian"
            updatedUser.lastname.value shouldBe "Mustermann"
            updatedUser.username.value shouldBe "mmuster"
            updatedUser.email.value shouldBe "max@test.de"
            ctx.logger.auditLogs.size shouldBe 1
        }

        "should update multiple fields at once" {
            val presenter = TestPresenter<Unit>()

            useCase.execute(
                EditUserRequestDto(
                    requestorId = adminId,
                    userId = targetUserId,
                    firstname = "Maximilian",
                    lastname = "Muster",
                    username = "mmuster2",
                    email = "maximilian@test.de",
                ),
                presenter,
            )

            presenter.error shouldBe null
            val updatedUser = ctx.userGateway.findById(UserId(targetUserId))
            updatedUser!!.firstname.value shouldBe "Maximilian"
            updatedUser.lastname.value shouldBe "Muster"
            updatedUser.username.value shouldBe "mmuster2"
            updatedUser.email.value shouldBe "maximilian@test.de"
        }

        "should reject edit by non-admin" {
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

            val presenter = TestPresenter<Unit>()
            useCase.execute(
                EditUserRequestDto(
                    requestorId = regularUser.id.value,
                    userId = targetUserId,
                    firstname = "Changed",
                ),
                presenter,
            )

            presenter.response shouldBe null
            presenter.error shouldNotBe null
            presenter.error!!.shouldBeInstanceOf<ErrorResponse.Unauthorized>()
        }

        "should reject edit of non-existent user" {
            val presenter = TestPresenter<Unit>()
            useCase.execute(
                EditUserRequestDto(
                    requestorId = adminId,
                    userId = "non-existent-id",
                    firstname = "Changed",
                ),
                presenter,
            )

            presenter.response shouldBe null
            presenter.error shouldNotBe null
            presenter.error!!.shouldBeInstanceOf<ErrorResponse.NotFound>()
        }

        "should reject edit with non-existent requestor" {
            val presenter = TestPresenter<Unit>()
            useCase.execute(
                EditUserRequestDto(
                    requestorId = "non-existent-id",
                    userId = targetUserId,
                    firstname = "Changed",
                ),
                presenter,
            )

            presenter.response shouldBe null
            presenter.error shouldNotBe null
            presenter.error!!.shouldBeInstanceOf<ErrorResponse.NotFound>()
        }

        "should reject duplicate email" {
            ctx.userGateway.save(
                User(
                    id = UserId.generate(),
                    role = UserRole.USER,
                    firstname = UserFirstname("Other"),
                    lastname = UserLastname("User"),
                    username = Username("other"),
                    email = UserEmail("other@test.de"),
                    passwordHash = PasswordHash(ctx.passwordHasher.hash("pass123")),
                    isActive = true,
                    mustChangePassword = false,
                ),
            )

            val presenter = TestPresenter<Unit>()
            useCase.execute(
                EditUserRequestDto(
                    requestorId = adminId,
                    userId = targetUserId,
                    email = "other@test.de",
                ),
                presenter,
            )

            presenter.response shouldBe null
            presenter.error shouldNotBe null
            presenter.error!!.shouldBeInstanceOf<ErrorResponse.AlreadyExists>()
            presenter.error!!.message shouldContain "email"
        }

        "should reject duplicate username" {
            ctx.userGateway.save(
                User(
                    id = UserId.generate(),
                    role = UserRole.USER,
                    firstname = UserFirstname("Other"),
                    lastname = UserLastname("User"),
                    username = Username("other"),
                    email = UserEmail("other@test.de"),
                    passwordHash = PasswordHash(ctx.passwordHasher.hash("pass123")),
                    isActive = true,
                    mustChangePassword = false,
                ),
            )

            val presenter = TestPresenter<Unit>()
            useCase.execute(
                EditUserRequestDto(
                    requestorId = adminId,
                    userId = targetUserId,
                    username = "other",
                ),
                presenter,
            )

            presenter.response shouldBe null
            presenter.error shouldNotBe null
            presenter.error!!.shouldBeInstanceOf<ErrorResponse.AlreadyExists>()
            presenter.error!!.message shouldContain "username"
        }

        "should allow setting own email (no uniqueness conflict)" {
            val presenter = TestPresenter<Unit>()
            useCase.execute(
                EditUserRequestDto(
                    requestorId = adminId,
                    userId = targetUserId,
                    email = "max@test.de",
                ),
                presenter,
            )

            presenter.error shouldBe null
        }

        "should allow setting own username (no uniqueness conflict)" {
            val presenter = TestPresenter<Unit>()
            useCase.execute(
                EditUserRequestDto(
                    requestorId = adminId,
                    userId = targetUserId,
                    username = "mmuster",
                ),
                presenter,
            )

            presenter.error shouldBe null
        }

        "should be a no-op when all fields are null" {
            val presenter = TestPresenter<Unit>()
            useCase.execute(
                EditUserRequestDto(
                    requestorId = adminId,
                    userId = targetUserId,
                ),
                presenter,
            )

            presenter.error shouldBe null
            val updatedUser = ctx.userGateway.findById(UserId(targetUserId))
            updatedUser!!.firstname.value shouldBe "Max"
            updatedUser.lastname.value shouldBe "Mustermann"
            updatedUser.username.value shouldBe "mmuster"
            updatedUser.email.value shouldBe "max@test.de"
        }
    })
