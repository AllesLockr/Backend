package com.alleslocker.backend.application.integration.user

import com.alleslocker.backend.application.common.ErrorResponse
import com.alleslocker.backend.application.integration.config.TestPresenter
import com.alleslocker.backend.application.integration.config.createUserTestContext
import com.alleslocker.backend.application.user.dto.request.GetUserRequestDto
import com.alleslocker.backend.application.user.dto.response.GetUserResponseDto
import com.alleslocker.backend.application.user.usecase.GetUserUseCase
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

class GetUserUseCaseTest :
    FreeSpec({
        val ctx = createUserTestContext()
        val useCase: GetUserUseCase = ctx.useCaseFactory.make(GetUserUseCase::class)

        lateinit var savedUserId: String

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
                        passwordHash = PasswordHash(ctx.passwordHasher.hash("secure123")),
                        isActive = true,
                        mustChangePassword = false,
                    ),
                )
            savedUserId = saved.id.value
        }

        "should return user by id" {
            val presenter = TestPresenter<GetUserResponseDto>()

            useCase.execute(GetUserRequestDto(id = savedUserId), presenter)

            presenter.error shouldBe null
            presenter.response shouldNotBe null
            presenter.response!!.user.id shouldBe savedUserId
            presenter.response!!.user.username shouldBe "mmuster"
            presenter.response!!.user.email shouldBe "max@test.de"
            presenter.response!!.user.firstname shouldBe "Max"
            presenter.response!!.user.lastname shouldBe "Mustermann"
            presenter.response!!.user.isActive shouldBe true
            presenter.response!!.user.mustChangePassword shouldBe false
        }

        "should return not found for non-existent id" {
            val presenter = TestPresenter<GetUserResponseDto>()

            useCase.execute(GetUserRequestDto(id = "non-existent-id"), presenter)

            presenter.response shouldBe null
            presenter.error shouldNotBe null
            presenter.error!!.shouldBeInstanceOf<ErrorResponse.NotFound>()
        }

        "should return bad request for empty id" {
            val presenter = TestPresenter<GetUserResponseDto>()

            useCase.execute(GetUserRequestDto(id = ""), presenter)

            presenter.response shouldBe null
            presenter.error shouldNotBe null
            presenter.error!!.shouldBeInstanceOf<ErrorResponse.BadRequest>()
        }
    })
