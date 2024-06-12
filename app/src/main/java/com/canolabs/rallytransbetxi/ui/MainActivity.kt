package com.canolabs.rallytransbetxi.ui

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.canolabs.rallytransbetxi.ui.maps.MapsScreenViewModel
import com.canolabs.rallytransbetxi.ui.miscellaneous.MainActivityViewModelFactory
import com.canolabs.rallytransbetxi.ui.navigation.Navigation
import com.canolabs.rallytransbetxi.ui.rally.RallyScreenViewModel
import com.canolabs.rallytransbetxi.ui.results.ResultsScreenViewModel
import com.canolabs.rallytransbetxi.ui.stages.StagesScreenViewModel
import com.canolabs.rallytransbetxi.ui.teams.TeamsScreenViewModel
import com.canolabs.rallytransbetxi.ui.theme.RallyTransbetxiTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.canolabs.rallytransbetxi.ui.miscellaneous.network.BottomSheetConnectivityLost
import com.canolabs.rallytransbetxi.ui.miscellaneous.network.ConnectivityObserver
import com.canolabs.rallytransbetxi.ui.miscellaneous.network.NetworkConnectivityObserver
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var viewModelFactory: MainActivityViewModelFactory
    private lateinit var connectivityObserver: ConnectivityObserver

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        connectivityObserver = NetworkConnectivityObserver(applicationContext)
        setContent {
            val darkThemeState = remember { mutableStateOf(false) }
            val fontScaleState = remember { mutableFloatStateOf(1f) }

            RallyTransbetxiTheme(
                darkTheme = darkThemeState,
                fontScale = fontScaleState
            ) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val stagesScreenViewModel: StagesScreenViewModel = viewModel(
                        factory = viewModelFactory
                    )

                    val teamsScreenViewModel: TeamsScreenViewModel = viewModel(
                        factory = viewModelFactory
                    )

                    val resultsScreenViewModel: ResultsScreenViewModel = viewModel(
                        factory = viewModelFactory
                    )

                    val mapsScreenViewModel: MapsScreenViewModel = viewModel(
                        factory = viewModelFactory
                    )

                    val rallyScreenViewModel: RallyScreenViewModel = viewModel(
                        factory = viewModelFactory
                    )

                    fun changeLocale(locale: String) {
                        changeLocale(this, locale)
                    }

                    val sharedPreferences = applicationContext.getSharedPreferences("MyApp", Context.MODE_PRIVATE)
                    val finishedOnboarding = remember { mutableStateOf(sharedPreferences.getBoolean("FinishedOnboarding", false)) }

                    Navigation(
                        stagesScreenViewModel = stagesScreenViewModel,
                        teamsScreenViewModel = teamsScreenViewModel,
                        resultsScreenViewModel = resultsScreenViewModel,
                        mapsScreenViewModel = mapsScreenViewModel,
                        rallyScreenViewModel = rallyScreenViewModel,
                        darkThemeState = darkThemeState,
                        fontScaleState = fontScaleState,
                        changeLocale = ::changeLocale,
                        finishedOnboarding = finishedOnboarding,
                        sharedPreferences = sharedPreferences
                    )

                    val networkStatus by connectivityObserver.observe().collectAsState(
                        initial = ConnectivityObserver.Status.Available
                    )

                    val bottomSheetState = rememberModalBottomSheetState()
                    val coroutineScope = rememberCoroutineScope()

                    if (networkStatus != ConnectivityObserver.Status.Available) {
                        ModalBottomSheet(
                            sheetState = bottomSheetState,
                            onDismissRequest = {
                                coroutineScope.launch {
                                    bottomSheetState.hide()
                                }
                            },
                        ) {
                            BottomSheetConnectivityLost(
                                onOmitButtonPressed = {
                                    coroutineScope.launch {
                                        bottomSheetState.hide()
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

fun changeLocale(context: Context, localeString: String) {
    val locale = Locale(localeString)
    Locale.setDefault(locale)
    val config = Configuration(context.resources.configuration)
    config.setLocale(locale)
    context.createConfigurationContext(config)
    context.resources.updateConfiguration(config, context.resources.displayMetrics)
}