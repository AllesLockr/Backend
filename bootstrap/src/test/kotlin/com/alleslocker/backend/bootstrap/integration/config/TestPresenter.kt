package com.alleslocker.backend.bootstrap.integration.config

import com.alleslocker.backend.application.common.ErrorResponse
import com.alleslocker.backend.application.common.OutputBoundary

class TestPresenter<R> : OutputBoundary<R> {
    var response: R? = null
    var error: ErrorResponse? = null

    override fun present(response: R) {
        this.response = response
    }

    override fun presentFailure(error: ErrorResponse) {
        this.error = error
    }
}
