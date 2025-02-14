package com.canolabs.rallytransbetxi.shared.di

import com.canolabs.rallytransbetxi.data.getDatabaseBuilder
import org.koin.dsl.module

actual val nativeModule = module {
    single { getDatabaseBuilder() }
}