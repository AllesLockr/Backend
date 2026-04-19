package com.alleslocker.backend.application.common

interface InputBoundary<RQ, RS> {
    fun execute(request: RQ, presenter: OutputBoundary<RS>)
}