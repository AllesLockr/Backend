package com.alleslocker.backend.application.integration.user

import com.alleslocker.backend.application.common.ErrorResponse
import com.alleslocker.backend.application.integration.config.TestPresenter
import com.alleslocker.backend.application.integration.config.createUserTestContext
import com.alleslocker.backend.application.user.dto.request.ResetPasswordUserRequestDto
import com.alleslocker.backend.application.user.dto.response.ResetPasswordUserResponseDto
import com.alleslocker.backend.application.user.usecase.ResetPasswordUserUseCase
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

class ResetPasswordUserUseCaseTest :
    FreeSpec({
        val ctx = createUserTestContext()
        val useCase: ResetPasswordUserUseCase = ctx.useCaseFactory.make(ResetPasswordUserUseCase::class)

        lateinit var userId: String

        beforeEach {
            ctx.userGateway.deleteAll()
            val saved =
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
            userId = saved.id.value
        }

        "should reset password with correct old password" {
            val presenter = TestPresenter<ResetPasswordUserResponseDto>()

            useCase.execute(
                ResetPasswordUserRequestDto(
                    requestorId = userId,
                    oldPassword = "oldPass123",
                    newPassword = "newPass456",
                ),
                presenter,
            )

            presenter.error shouldBe null
            presenter.response shouldNotBe null
            presenter.response!!.userId shouldBe userId

            val updatedUser = ctx.userGateway.findById(UserId(userId))
            updatedUser!!.mustChangePassword shouldBe false
            ctx.passwordHasher.verify("newPass456", updatedUser.passwordHash.value) shouldBe true
            ctx.passwordHasher.verify("oldPass123", updatedUser.passwordHash.value) shouldBe false
        }

        "should reject wrong old password" {
            val presenter = TestPresenter<ResetPasswordUserResponseDto>()

            useCase.execute(
                ResetPasswordUserRequestDto(
                    requestorId = userId,
                    oldPassword = "wrongPassword",
                    newPassword = "newPass456",
                ),
                presenter,
            )

            presenter.response shouldBe null
            presenter.error shouldNotBe null
            presenter.error!!.shouldBeInstanceOf<ErrorResponse.Unauthorized>()
            presenter.error!!.message shouldBe "Invalid old password"
        }

        "should reject reset for non-existent user" {
            val presenter = TestPresenter<ResetPasswordUserResponseDto>()

            useCase.execute(
                ResetPasswordUserRequestDto(
                    requestorId = "non-existent-id",
                    oldPassword = "oldPass123",
                    newPassword = "newPass456",
                ),
                presenter,
            )

            presenter.response shouldBe null
            presenter.error shouldNotBe null
            presenter.error!!.shouldBeInstanceOf<ErrorResponse.NotFound>()
        }
    })
