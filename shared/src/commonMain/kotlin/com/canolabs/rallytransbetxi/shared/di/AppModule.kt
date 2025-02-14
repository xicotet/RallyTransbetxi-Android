package com.canolabs.rallytransbetxi.shared.di


import com.canolabs.rallytransbetxi.shared.data.repositories.VersionsRepository
import com.canolabs.rallytransbetxi.shared.data.repositories.VersionsRepositoryImpl
import com.canolabs.rallytransbetxi.shared.data.sources.remote.VersionsService
import com.canolabs.rallytransbetxi.shared.data.sources.remote.VersionsServiceImpl
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

val appModule = module {
    singleOf(::VersionsRepositoryImpl) { bind<VersionsRepository>() }
    singleOf(::VersionsServiceImpl) { bind<VersionsService>() }
}

expect val nativeModule: Module

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(appModule, nativeModule)
    }
}