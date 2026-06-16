package com.alleslocker.backend.application.integration.user

import com.alleslocker.backend.application.common.ErrorResponse
import com.alleslocker.backend.application.integration.config.TestPresenter
import com.alleslocker.backend.application.integration.config.createUserTestContext
import com.alleslocker.backend.application.user.dto.request.LoginUserRequestDto
import com.alleslocker.backend.application.user.dto.response.LoginUserResponseDto
import com.alleslocker.backend.application.user.usecase.LoginUserUseCase
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

class LoginUserUseCaseTest :
    FreeSpec({
        val ctx = createUserTestContext()
        val useCase: LoginUserUseCase = ctx.useCaseFactory.make(LoginUserUseCase::class)

        beforeEach {
            ctx.userGateway.deleteAll()
            ctx.userGateway.save(
                User(
                    id = UserId.generate(),
                    role = UserRole.USER,
                    firstname = UserFirstname("Max"),
                    lastname = UserLastname("Mustermann"),
                    username = Username("mmuster"),
                    email = UserEmail("max@test.de"),
                    passwordHash = PasswordHash(ctx.passwordHasher.hash("secure123")),
                    isActive = true,
                    mustChangePassword = false,
                ),
            )
        }

        "should login with correct credentials" {
            val presenter = TestPresenter<LoginUserResponseDto>()

            useCase.execute(LoginUserRequestDto(username = "mmuster", password = "secure123"), presenter)

            presenter.error shouldBe null
            presenter.response shouldNotBe null
            presenter.response!!.userId shouldNotBe ""
        }

        "should fail with wrong password" {
            val presenter = TestPresenter<LoginUserResponseDto>()

            useCase.execute(LoginUserRequestDto(username = "mmuster", password = "wrong"), presenter)

            presenter.response shouldBe null
            presenter.error shouldNotBe null
            presenter.error!!.shouldBeInstanceOf<ErrorResponse.BadRequest>()
            presenter.error!!.message shouldBe "Invalid password"
        }

        "should fail with non-existent username" {
            val presenter = TestPresenter<LoginUserResponseDto>()

            useCase.execute(LoginUserRequestDto(username = "nobody", password = "secure123"), presenter)

            presenter.response shouldBe null
            presenter.error shouldNotBe null
            presenter.error!!.shouldBeInstanceOf<ErrorResponse.NotFound>()
            presenter.error!!.message shouldBe "User doesn't exist"
        }

        "should fail with empty username" {
            val presenter = TestPresenter<LoginUserResponseDto>()

            useCase.execute(LoginUserRequestDto(username = "", password = "secure123"), presenter)

            presenter.response shouldBe null
            presenter.error shouldNotBe null
        }
    })
