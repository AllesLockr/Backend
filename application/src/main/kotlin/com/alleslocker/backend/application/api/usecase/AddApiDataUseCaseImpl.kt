package com.alleslocker.backend.application.api.usecase

import com.alleslocker.backend.application.api.dto.request.AddApiDataRequestDto
import com.alleslocker.backend.application.api.gateway.ApiDataGateway
import com.alleslocker.backend.application.common.ErrorResponse
import com.alleslocker.backend.application.common.OutputBoundary
import com.alleslocker.backend.application.common.SuccessResponse
import com.alleslocker.backend.domain.api.*
import java.net.URI
import java.net.URISyntaxException

class AddApiDataUseCaseImpl(
    private val apiDataGateway: ApiDataGateway,
) : AddApiDataUseCase {
    override fun execute(
        request: AddApiDataRequestDto,
        presenter: OutputBoundary<SuccessResponse>
    ) {
        val forApi = try {
            AvailableApis.valueOf(request.forApi)
        } catch (_: IllegalArgumentException) {
            presenter.presentFailure(ErrorResponse.UnprocessableEntity("${request.forApi} is not implemented (yet)!"))
            return
        }

        val baseUrl = try {
            URI(request.baseUrl)
        } catch (_: URISyntaxException) {
            presenter.presentFailure(ErrorResponse.BadRequest("baseUrl is not a valid URI"))
            return
        } catch (_: NullPointerException) {
            presenter.presentFailure(ErrorResponse.BadRequest("baseUrl must not be null"))
            return
        }

        val apiAuthentication = let {
            if (!request.apiKey.isNullOrBlank() && request.apiUsername.isNullOrBlank() && request.apiPassword.isNullOrBlank()) {
                ApiAuthentication.ApiKey(request.apiKey)
            } else if (request.apiKey.isNullOrBlank() && !request.apiUsername.isNullOrBlank() && !request.apiPassword.isNullOrBlank()) {
                ApiAuthentication.BaseAuth(ApiUsername(request.apiUsername), ApiPassword(request.apiPassword))
            } else {
                return presenter.presentFailure(ErrorResponse.BadRequest("either apiKey or apiUsername and apiPassword must be set!"))
            }
        }

        val apiData =
            ApiData(id = ApiId.generate(), forApi = forApi, baseUrl = baseUrl, apiAuthentication = apiAuthentication)

        val saved = try {
            apiDataGateway.save(apiData)
        } catch (e: Exception) {
            return presenter.presentFailure(ErrorResponse.InternalServerError("Could not save api to db: ${e.message}"))
        }

        presenter.present(SuccessResponse.Created("Successfully added ${saved.baseUrl}"))
    }
}