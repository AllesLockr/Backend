package com.alleslocker.backend.application.common.factory

import com.alleslocker.backend.application.common.adapter.Adapter
import kotlin.reflect.KClass

interface AdapterFactory {
    fun <T : Adapter> make(adapter: KClass<out T>): T
    operator fun <T : Adapter> get(adapter: KClass<out T>) = make(adapter)
}