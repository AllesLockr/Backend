package com.alleslocker.backend.application.integration.user

import com.alleslocker.backend.application.common.ErrorResponse
import com.alleslocker.backend.application.integration.config.TestPresenter
import com.alleslocker.backend.application.integration.config.createUserTestContext
import com.alleslocker.backend.application.user.dto.request.DeactivateUserRequestDto
import com.alleslocker.backend.application.user.usecase.DeactivateUserUseCase
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

class DeactivateUserUseCaseTest :
    FreeSpec({
        val ctx = createUserTestContext()
        val useCase: DeactivateUserUseCase = ctx.useCaseFactory.make(DeactivateUserUseCase::class)

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

        "should deactivate an active user" {
            val presenter = TestPresenter<Unit>()

            useCase.execute(
                DeactivateUserRequestDto(requestorId = adminId, userId = targetUserId),
                presenter,
            )

            presenter.error shouldBe null
            val updatedUser = ctx.userGateway.findById(UserId(targetUserId))
            updatedUser!!.isActive shouldBe false
            ctx.logger.auditLogs.size shouldBe 1
        }

        "should reject deactivating already deactivated user" {
            ctx.userGateway.save(
                ctx.userGateway.findById(UserId(targetUserId))!!.copy(isActive = false),
            )

            val presenter = TestPresenter<Unit>()
            useCase.execute(
                DeactivateUserRequestDto(requestorId = adminId, userId = targetUserId),
                presenter,
            )

            presenter.response shouldBe null
            presenter.error shouldNotBe null
            presenter.error!!.shouldBeInstanceOf<ErrorResponse.BadRequest>()
        }

        "should reject deactivation by non-admin" {
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
                DeactivateUserRequestDto(requestorId = regularUser.id.value, userId = targetUserId),
                presenter,
            )

            presenter.response shouldBe null
            presenter.error shouldNotBe null
            presenter.error!!.shouldBeInstanceOf<ErrorResponse.Unauthorized>()
        }

        "should reject deactivation of non-existent user" {
            val presenter = TestPresenter<Unit>()
            useCase.execute(
                DeactivateUserRequestDto(requestorId = adminId, userId = "non-existent-id"),
                presenter,
            )

            presenter.response shouldBe null
            presenter.error shouldNotBe null
            presenter.error!!.shouldBeInstanceOf<ErrorResponse.NotFound>()
        }

        "should reject deactivation with non-existent requestor" {
            val presenter = TestPresenter<Unit>()
            useCase.execute(
                DeactivateUserRequestDto(requestorId = "non-existent-id", userId = targetUserId),
                presenter,
            )

            presenter.response shouldBe null
            presenter.error shouldNotBe null
            presenter.error!!.shouldBeInstanceOf<ErrorResponse.NotFound>()
        }
    })
