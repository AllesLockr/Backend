package com.alleslocker.backend.application.integration.user

import com.alleslocker.backend.application.common.ErrorResponse
import com.alleslocker.backend.application.integration.config.TestPresenter
import com.alleslocker.backend.application.integration.config.createUserTestContext
import com.alleslocker.backend.application.user.dto.request.ActivateUserRequestDto
import com.alleslocker.backend.application.user.usecase.ActivateUserUseCase
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
import io.kotest.matchers.types.shouldBeInstanceOf

class ActivateUserUseCaseTest :
    FreeSpec({
        val ctx = createUserTestContext()
        val useCase: ActivateUserUseCase = ctx.useCaseFactory.make(ActivateUserUseCase::class)

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
                        isActive = false,
                        mustChangePassword = false,
                    ),
                )
            targetUserId = targetUser.id.value
        }

        "should activate a deactivated user" {
            val presenter = TestPresenter<Unit>()

            useCase.execute(
                ActivateUserRequestDto(requestorId = adminId, userId = targetUserId),
                presenter,
            )

            presenter.error shouldBe null
            val updatedUser = ctx.userGateway.findById(UserId(targetUserId))
            updatedUser!!.isActive shouldBe true
            ctx.logger.auditLogs.size shouldBe 1
        }

        "should reject activating already active user" {
            ctx.userGateway.save(
                ctx.userGateway.findById(UserId(targetUserId))!!.copy(isActive = true),
            )

            val presenter = TestPresenter<Unit>()
            useCase.execute(
                ActivateUserRequestDto(requestorId = adminId, userId = targetUserId),
                presenter,
            )

            presenter.response shouldBe null
            presenter.error shouldNotBe null
            presenter.error!!.shouldBeInstanceOf<ErrorResponse.BadRequest>()
        }

        "should reject activation by non-admin" {
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
                ActivateUserRequestDto(requestorId = regularUser.id.value, userId = targetUserId),
                presenter,
            )

            presenter.response shouldBe null
            presenter.error shouldNotBe null
            presenter.error!!.shouldBeInstanceOf<ErrorResponse.Unauthorized>()
        }

        "should reject activation of non-existent user" {
            val presenter = TestPresenter<Unit>()
            useCase.execute(
                ActivateUserRequestDto(requestorId = adminId, userId = "non-existent-id"),
                presenter,
            )

            presenter.response shouldBe null
            presenter.error shouldNotBe null
            presenter.error!!.shouldBeInstanceOf<ErrorResponse.NotFound>()
        }

        "should reject activation with non-existent requestor" {
            val presenter = TestPresenter<Unit>()
            useCase.execute(
                ActivateUserRequestDto(requestorId = "non-existent-id", userId = targetUserId),
                presenter,
            )

            presenter.response shouldBe null
            presenter.error shouldNotBe null
            presenter.error!!.shouldBeInstanceOf<ErrorResponse.NotFound>()
        }
    })
