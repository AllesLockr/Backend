package eu.luftiger.cae.application.common

interface InputBoundary<RQ, RS> {
    fun execute(request: RQ, presenter: OutputBoundary<RS>)
}