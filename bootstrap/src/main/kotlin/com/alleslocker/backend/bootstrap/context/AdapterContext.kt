package com.alleslocker.backend.bootstrap.context

import com.alleslocker.backend.application.common.adapter.Adapter
import com.alleslocker.backend.application.common.factory.AdapterFactory
import com.alleslocker.backend.lockconnector.factory.AdapterFactoryImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
internal open class AdapterContext {
    @Bean
    open fun adapterFactory(adapters: List<Adapter>): AdapterFactory = AdapterFactoryImpl(adapters)
}