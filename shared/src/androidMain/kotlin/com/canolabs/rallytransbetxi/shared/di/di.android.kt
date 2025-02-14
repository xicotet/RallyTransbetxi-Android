package com.canolabs.rallytransbetxi.shared.di

import com.canolabs.rallytransbetxi.data.getDatabaseBuilder
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual val nativeModule = module {
    singleOf(::getDatabaseBuilder)
}