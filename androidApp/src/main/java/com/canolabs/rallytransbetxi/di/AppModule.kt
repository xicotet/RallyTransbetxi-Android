package com.canolabs.rallytransbetxi.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.canolabs.rallytransbetxi.data.models.responses.AppSetting
import androidx.sqlite.db.SupportSQLiteDatabase
import com.canolabs.rallytransbetxi.BuildConfig
import com.canolabs.rallytransbetxi.domain.usecases.GetTeamsUseCase
import com.canolabs.rallytransbetxi.domain.usecases.GetActivitiesUseCase
import com.canolabs.rallytransbetxi.domain.usecases.InsertSettingsUseCase
import com.canolabs.rallytransbetxi.domain.usecases.GetDirectionsUseCase
import com.canolabs.rallytransbetxi.domain.usecases.CanAccessToAppUseCase
import com.canolabs.rallytransbetxi.domain.usecases.GetAreNewsCollapsedUseCase
import com.canolabs.rallytransbetxi.domain.usecases.GetAreStatementsCollapsedUseCase
import com.canolabs.rallytransbetxi.domain.usecases.GetAreActivitiesCollapsedUseCase
import com.canolabs.rallytransbetxi.domain.usecases.GetStageRaceWarningUseCase
import com.canolabs.rallytransbetxi.domain.usecases.GetGlobalRaceWarningUseCase
import com.canolabs.rallytransbetxi.domain.usecases.GetThemeSettingsUseCase
import com.canolabs.rallytransbetxi.domain.usecases.GetFontSizeFactorSettingsUseCase
import com.canolabs.rallytransbetxi.domain.usecases.GetBetxiRestaurantsUseCase
import com.canolabs.rallytransbetxi.domain.usecases.GetGlobalResultsUseCase
import com.canolabs.rallytransbetxi.domain.usecases.GetHallOfFameUseCase
import com.canolabs.rallytransbetxi.domain.usecases.GetNewsUseCase
import com.canolabs.rallytransbetxi.domain.usecases.GetNotificationPermissionCounterUseCase
import com.canolabs.rallytransbetxi.domain.usecases.GetProfileSettingsUseCase
import com.canolabs.rallytransbetxi.domain.usecases.GetNumberOfSponsorsUseCase
import com.canolabs.rallytransbetxi.domain.usecases.GetRestaurantsUseCase
import com.canolabs.rallytransbetxi.domain.usecases.GetStageByAcronymUseCase
import com.canolabs.rallytransbetxi.domain.usecases.GetStagesUseCase
import com.canolabs.rallytransbetxi.domain.usecases.GetStagesResultsUseCase
import com.canolabs.rallytransbetxi.domain.usecases.GetStatementsUseCase
import com.canolabs.rallytransbetxi.data.sources.remote.CategoriesServiceImpl
import com.canolabs.rallytransbetxi.data.sources.remote.VersionsServiceImpl
import com.canolabs.rallytransbetxi.data.sources.remote.ActivitiesServiceImpl
import com.canolabs.rallytransbetxi.data.sources.remote.NewsServiceImpl
import com.canolabs.rallytransbetxi.data.sources.remote.HallOfFameServiceImpl
import com.canolabs.rallytransbetxi.data.sources.remote.RestaurantsServiceImpl
import com.canolabs.rallytransbetxi.data.sources.remote.StatementsServiceImpl
import com.canolabs.rallytransbetxi.data.sources.remote.RaceWarningsServiceImpl
import com.canolabs.rallytransbetxi.data.sources.remote.TeamsServiceImpl
import com.canolabs.rallytransbetxi.data.sources.remote.ResultsServiceImpl
import com.canolabs.rallytransbetxi.data.sources.remote.SponsorsServiceImpl
import com.canolabs.rallytransbetxi.data.sources.remote.StagesServiceImpl
import com.canolabs.rallytransbetxi.data.sources.remote.VersionsService
import com.canolabs.rallytransbetxi.data.sources.remote.ActivitiesService
import com.canolabs.rallytransbetxi.data.sources.remote.CategoriesService
import com.canolabs.rallytransbetxi.data.sources.remote.NewsService
import com.canolabs.rallytransbetxi.data.sources.remote.HallOfFameService
import com.canolabs.rallytransbetxi.data.sources.remote.RestaurantsService
import com.canolabs.rallytransbetxi.data.sources.remote.StatementsService
import com.canolabs.rallytransbetxi.data.sources.remote.RaceWarningsService
import com.canolabs.rallytransbetxi.data.sources.remote.TeamsService
import com.canolabs.rallytransbetxi.data.sources.remote.ResultsService
import com.canolabs.rallytransbetxi.data.sources.remote.SponsorsService
import com.canolabs.rallytransbetxi.data.sources.remote.StagesService
import com.canolabs.rallytransbetxi.data.repositories.ActivitiesRepository
import com.canolabs.rallytransbetxi.data.repositories.ActivitiesRepositoryImpl
import com.canolabs.rallytransbetxi.data.repositories.CategoriesRepository
import com.canolabs.rallytransbetxi.data.repositories.CategoriesRepositoryImpl
import com.canolabs.rallytransbetxi.data.repositories.TeamsRepositoryImpl
import com.canolabs.rallytransbetxi.data.repositories.VersionsRepositoryImpl
import com.canolabs.rallytransbetxi.data.repositories.TeamsRepository
import com.canolabs.rallytransbetxi.data.repositories.VersionsRepository
import com.canolabs.rallytransbetxi.data.repositories.AppSettingsRepository
import com.canolabs.rallytransbetxi.data.repositories.AppSettingsRepositoryImpl
import com.canolabs.rallytransbetxi.data.repositories.NewsRepository
import com.canolabs.rallytransbetxi.data.repositories.NewsRepositoryImpl
import com.canolabs.rallytransbetxi.data.repositories.HallOfFameRepository
import com.canolabs.rallytransbetxi.data.repositories.HallOfFameRepositoryImpl
import com.canolabs.rallytransbetxi.data.repositories.RestaurantsRepository
import com.canolabs.rallytransbetxi.data.repositories.RestaurantsRepositoryImpl
import com.canolabs.rallytransbetxi.data.repositories.StatementsRepository
import com.canolabs.rallytransbetxi.data.repositories.StatementsRepositoryImpl
import com.canolabs.rallytransbetxi.data.repositories.RaceWarningsRepository
import com.canolabs.rallytransbetxi.data.repositories.RaceWarningsRepositoryImpl
import com.canolabs.rallytransbetxi.data.repositories.ResultsRepository
import com.canolabs.rallytransbetxi.data.repositories.ResultsRepositoryImpl
import com.canolabs.rallytransbetxi.data.repositories.AppVersionRepository
import com.canolabs.rallytransbetxi.data.repositories.AppVersionRepositoryImpl
import com.canolabs.rallytransbetxi.data.repositories.DirectionsRepository
import com.canolabs.rallytransbetxi.data.repositories.DirectionsRepositoryImpl
import com.canolabs.rallytransbetxi.data.repositories.PlacesRepository
import com.canolabs.rallytransbetxi.data.repositories.PlacesRepositoryImpl
import com.canolabs.rallytransbetxi.data.repositories.StagesRepository
import com.canolabs.rallytransbetxi.data.repositories.StagesRepositoryImpl
import com.canolabs.rallytransbetxi.data.repositories.SponsorsRepository
import com.canolabs.rallytransbetxi.data.repositories.SponsorsRepositoryImpl
import com.canolabs.rallytransbetxi.data.sources.local.database.AppDatabase
import com.canolabs.rallytransbetxi.data.sources.remote.DirectionsService
import com.canolabs.rallytransbetxi.data.sources.remote.PlacesService
import com.canolabs.rallytransbetxi.ui.maps.MapsScreenViewModel
import com.canolabs.rallytransbetxi.ui.rally.RallyScreenViewModel
import com.canolabs.rallytransbetxi.ui.results.ResultsScreenViewModel
import com.canolabs.rallytransbetxi.ui.stages.StagesScreenViewModel
import com.canolabs.rallytransbetxi.ui.teams.TeamsScreenViewModel
import com.canolabs.rallytransbetxi.ui.miscellaneous.network.NetworkChecker
import com.canolabs.rallytransbetxi.ui.miscellaneous.network.NetworkCheckerImpl
import com.canolabs.rallytransbetxi.utils.Constants
import com.canolabs.rallytransbetxi.utils.Constants.Companion.DATABASE_NAME
import com.canolabs.rallytransbetxi.utils.Constants.Companion.DEFAULT_ACTIVITIES_COLLAPSED
import com.canolabs.rallytransbetxi.utils.Constants.Companion.DEFAULT_FONT_SIZE_FACTOR
import com.canolabs.rallytransbetxi.utils.Constants.Companion.DEFAULT_NEWS_COLLAPSED
import com.canolabs.rallytransbetxi.utils.Constants.Companion.DEFAULT_NOTIFICATION_PERMISSION_COUNTER
import com.canolabs.rallytransbetxi.utils.Constants.Companion.DEFAULT_PROFILE
import com.canolabs.rallytransbetxi.utils.Constants.Companion.DEFAULT_THEME
import com.canolabs.rallytransbetxi.utils.Constants.Companion.DEFAULT_WARNINGS_COLLAPSED
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val appModule = module {
    single(named("directionsApiKey")) { BuildConfig.DIRECTIONS_API_KEY }
    single(named("mapsApiKey")) { BuildConfig.MAPS_API_KEY }

    // ViewModels
    viewModelOf(::MapsScreenViewModel)
    viewModelOf(::RallyScreenViewModel)
    viewModelOf(::StagesScreenViewModel)
    viewModelOf(::TeamsScreenViewModel)
    viewModelOf(::ResultsScreenViewModel)

    // UseCases singletons
    single { GetTeamsUseCase(get()) }
    single { GetActivitiesUseCase(get()) }
    single { InsertSettingsUseCase(get()) }
    single { GetDirectionsUseCase(get()) }
    single { CanAccessToAppUseCase(get()) }
    single { GetAreNewsCollapsedUseCase(get()) }
    single { GetAreStatementsCollapsedUseCase(get()) }
    single { GetAreActivitiesCollapsedUseCase(get()) }
    single { GetStageRaceWarningUseCase(get()) }
    single { GetGlobalRaceWarningUseCase(get()) }
    single { GetThemeSettingsUseCase(get()) }
    single { GetFontSizeFactorSettingsUseCase(get()) }
    single { GetBetxiRestaurantsUseCase(get()) }
    single { GetGlobalResultsUseCase(get()) }
    single { GetHallOfFameUseCase(get()) }
    single { GetNewsUseCase(get()) }
    single { GetNotificationPermissionCounterUseCase(get()) }
    single { GetProfileSettingsUseCase(get()) }
    single { GetNumberOfSponsorsUseCase(get()) }
    single { GetRestaurantsUseCase(get()) }
    single { GetStageByAcronymUseCase(get()) }
    single { GetStagesUseCase(get()) }
    single { GetStagesResultsUseCase(get()) }
    single { GetStatementsUseCase(get()) }

    // Repositories singletons
    singleOf(::CategoriesRepositoryImpl) { bind<CategoriesRepository>() }
    singleOf(::ActivitiesRepositoryImpl) { bind<ActivitiesRepository>() }
    singleOf(::TeamsRepositoryImpl) { bind<TeamsRepository>() }
    singleOf(::ResultsRepositoryImpl) { bind<ResultsRepository>() }
    singleOf(::VersionsRepositoryImpl) { bind<VersionsRepository>() }
    singleOf(::StagesRepositoryImpl) { bind<StagesRepository>() }
    singleOf(::NewsRepositoryImpl) { bind<NewsRepository>() }
    singleOf(::HallOfFameRepositoryImpl) { bind<HallOfFameRepository>() }
    singleOf(::RestaurantsRepositoryImpl) { bind<RestaurantsRepository>() }
    singleOf(::StatementsRepositoryImpl) { bind<StatementsRepository>() }
    singleOf(::RaceWarningsRepositoryImpl) { bind<RaceWarningsRepository>() }
    singleOf(::AppSettingsRepositoryImpl) { bind<AppSettingsRepository>() }
    singleOf(::AppVersionRepositoryImpl) { bind<AppVersionRepository>() }
    singleOf(::DirectionsRepositoryImpl) { bind<DirectionsRepository>() }
    singleOf(::PlacesRepositoryImpl) { bind<PlacesRepository>() }
    singleOf(::SponsorsRepositoryImpl) { bind<SponsorsRepository>() }

    // Services singletons
    singleOf(::CategoriesServiceImpl) { bind<CategoriesService>() }
    singleOf(::ActivitiesServiceImpl) { bind<ActivitiesService>() }
    singleOf(::TeamsServiceImpl) { bind<TeamsService>() }
    singleOf(::ResultsServiceImpl) { bind<ResultsService>() }
    singleOf(::VersionsServiceImpl) { bind<VersionsService>() }
    singleOf(::StagesServiceImpl) { bind<StagesService>() }
    singleOf(::NewsServiceImpl) { bind<NewsService>() }
    singleOf(::HallOfFameServiceImpl) { bind<HallOfFameService>() }
    singleOf(::RestaurantsServiceImpl) { bind<RestaurantsService>() }
    singleOf(::StatementsServiceImpl) { bind<StatementsService>() }
    singleOf(::RaceWarningsServiceImpl) { bind<RaceWarningsService>() }
    singleOf(::SponsorsServiceImpl) { bind<SponsorsService>() }
}

val firebaseModule = module {
    single { FirebaseFirestore.getInstance() }
}

val databaseModule = module {
    single {
        Room.databaseBuilder(
            get<Context>(), // Inject context
            AppDatabase::class.java,
            DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .addCallback(object : RoomDatabase.Callback() {
                override fun onOpen(db: SupportSQLiteDatabase) {
                    super.onOpen(db)
                    CoroutineScope(Dispatchers.IO).launch {
                        val settingsDao = get<AppDatabase>().settingsDao()
                        val count = settingsDao.getSettingCount()
                        if (count == 0) {
                            settingsDao.insertSetting(
                                AppSetting(
                                    1,
                                    DEFAULT_THEME,
                                    DEFAULT_PROFILE,
                                    DEFAULT_FONT_SIZE_FACTOR,
                                    DEFAULT_NOTIFICATION_PERMISSION_COUNTER,
                                    DEFAULT_WARNINGS_COLLAPSED,
                                    DEFAULT_NEWS_COLLAPSED,
                                    DEFAULT_ACTIVITIES_COLLAPSED
                                )
                            )
                        }
                    }
                }
            })
            .build()
    }
    single { get<AppDatabase>().settingsDao() }
    single { get<AppDatabase>().versionsDao() }
    single { get<AppDatabase>().stagesDao() }
    single { get<AppDatabase>().categoriesDao() }
    single { get<AppDatabase>().teamsDao() }
    single { get<AppDatabase>().resultsDao() }
    single { get<AppDatabase>().activitiesDao() }
    single { get<AppDatabase>().newsDao() }
    single { get<AppDatabase>().hallOfFameDao() }
    single { get<AppDatabase>().restaurantDao() }
    single { get<AppDatabase>().statementsDao() }
    single { get<AppDatabase>().raceWarningDao() }
}

val networkModule = module {
    single(named("DirectionsRetrofit")) { provideRetrofit(Constants.DIRECTIONS_BASE_URL) }
    single(named("PlacesRetrofit")) { provideRetrofit(Constants.PLACES_BASE_URL) }

    single { get<Retrofit>(named("DirectionsRetrofit")).create(DirectionsService::class.java) }
    single { get<Retrofit>(named("PlacesRetrofit")).create(PlacesService::class.java) }

    single { provideNetworkChecker(get()) }
}

fun provideRetrofit(baseUrl: String): Retrofit {
    val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
    val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
}

fun provideNetworkChecker(context: Context): NetworkChecker {
    return NetworkCheckerImpl(context)
}