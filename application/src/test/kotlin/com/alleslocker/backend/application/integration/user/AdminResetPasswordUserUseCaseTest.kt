package com.alleslocker.backend.application.integration.user

import com.alleslocker.backend.application.common.ErrorResponse
import com.alleslocker.backend.application.integration.config.TestPresenter
import com.alleslocker.backend.application.integration.config.createUserTestContext
import com.alleslocker.backend.application.user.dto.request.AdminResetPasswordUserRequestDto
import com.alleslocker.backend.application.user.dto.response.AdminResetPasswordUserResponseDto
import com.alleslocker.backend.application.user.usecase.AdminResetPasswordUserUseCase
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
import io.kotest.matchers.types.shouldBeInstanceOf

class AdminResetPasswordUserUseCaseTest :
    FreeSpec({
        val ctx = createUserTestContext()
        val useCase: AdminResetPasswordUserUseCase = ctx.useCaseFactory.make(AdminResetPasswordUserUseCase::class)

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
                        passwordHash = PasswordHash(ctx.passwordHasher.hash("oldPass123")),
                        isActive = true,
                        mustChangePassword = false,
                    ),
                )
            targetUserId = targetUser.id.value
        }

        "should reset password for user" {
            val presenter = TestPresenter<AdminResetPasswordUserResponseDto>()

            useCase.execute(
                AdminResetPasswordUserRequestDto(requestorId = adminId, userId = targetUserId),
                presenter,
            )

            presenter.error shouldBe null
            presenter.response shouldNotBe null
            presenter.response!!.userId shouldBe targetUserId
            presenter.response!!.password.length shouldBeGreaterThan 0

            val updatedUser = ctx.userGateway.findById(UserId(targetUserId))
            updatedUser!!.mustChangePassword shouldBe true
            ctx.passwordHasher.verify(presenter.response!!.password, updatedUser.passwordHash.value) shouldBe true

            ctx.logger.auditLogs.size shouldBe 1
        }

        "should reject reset by non-admin" {
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

            val presenter = TestPresenter<AdminResetPasswordUserResponseDto>()
            useCase.execute(
                AdminResetPasswordUserRequestDto(requestorId = regularUser.id.value, userId = targetUserId),
                presenter,
            )

            presenter.response shouldBe null
            presenter.error shouldNotBe null
            presenter.error!!.shouldBeInstanceOf<ErrorResponse.Unauthorized>()
        }

        "should reject reset of non-existent user" {
            val presenter = TestPresenter<AdminResetPasswordUserResponseDto>()
            useCase.execute(
                AdminResetPasswordUserRequestDto(requestorId = adminId, userId = "non-existent-id"),
                presenter,
            )

            presenter.response shouldBe null
            presenter.error shouldNotBe null
            presenter.error!!.shouldBeInstanceOf<ErrorResponse.NotFound>()
        }

        "should reject reset with non-existent requestor" {
            val presenter = TestPresenter<AdminResetPasswordUserResponseDto>()
            useCase.execute(
                AdminResetPasswordUserRequestDto(requestorId = "non-existent-id", userId = targetUserId),
                presenter,
            )

            presenter.response shouldBe null
            presenter.error shouldNotBe null
            presenter.error!!.shouldBeInstanceOf<ErrorResponse.NotFound>()
        }

        "generated password should be at least 8 characters" {
            val presenter = TestPresenter<AdminResetPasswordUserResponseDto>()
            useCase.execute(
                AdminResetPasswordUserRequestDto(requestorId = adminId, userId = targetUserId),
                presenter,
            )

            presenter.error shouldBe null
            presenter.response!!.password.length shouldBeGreaterThanOrEqualTo 8
        }
    })
