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
import androidx.compose.runtime.MutableState
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
import android.util.Log
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.lifecycleScope
import com.canolabs.rallytransbetxi.ui.miscellaneous.network.BottomSheetConnectivityLost
import com.canolabs.rallytransbetxi.ui.miscellaneous.network.ConnectivityObserver
import com.canolabs.rallytransbetxi.ui.miscellaneous.network.NetworkConnectivityObserver
import com.canolabs.rallytransbetxi.utils.Constants.Companion.SPLASH_SCREEN_DURATION
import com.google.firebase.Firebase
import com.google.firebase.appcheck.appCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.google.firebase.initialize
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var viewModelFactory: MainActivityViewModelFactory
    private lateinit var connectivityObserver: ConnectivityObserver

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("MainActivity", "Proceeding to: Firebase initialization")
        Firebase.initialize(context = this)
        Log.d("MainActivity", "Proceeding to: Firebase initialized")
        Firebase.appCheck.getAppCheckToken(false).addOnSuccessListener {
            Log.d("MainActivity", "App Check token: ${it.token}")
        }.addOnFailureListener {
            Log.d("MainActivity", "App Check token error: ${it.message}")
        }
        Firebase.appCheck.installAppCheckProviderFactory(
            PlayIntegrityAppCheckProviderFactory.getInstance(),
        )
        Log.d("MainActivity", "Proceeding to: App Check provider factory installed")

        val splashScreen = installSplashScreen()

        // Add a delay before dismissing the splash screen so the local data can be loaded
        lifecycleScope.launch {
            splashScreen.setKeepOnScreenCondition { true }
            delay(SPLASH_SCREEN_DURATION)
            splashScreen.setKeepOnScreenCondition { false }
        }

        // Load the selected language from SharedPreferences and apply it
        val sharedPreferences = getSharedPreferences("MyApp", Context.MODE_PRIVATE)
        val selectedLanguage =
            sharedPreferences.getString("SelectedLanguage", Locale.getDefault().language)
        if (selectedLanguage != null) {
            changeAppLocale(this, selectedLanguage)
        }

        connectivityObserver = NetworkConnectivityObserver(applicationContext)
        setContent {
            val darkThemeState = remember { mutableStateOf(false) }
            val fontScaleState = remember { mutableFloatStateOf(1f) }
            val recomposeNavbar = remember { mutableStateOf(false) }

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
                        changeAppLocale(this, locale, recomposeNavbar)
                    }

                    Navigation(
                        stagesScreenViewModel = stagesScreenViewModel,
                        teamsScreenViewModel = teamsScreenViewModel,
                        resultsScreenViewModel = resultsScreenViewModel,
                        mapsScreenViewModel = mapsScreenViewModel,
                        rallyScreenViewModel = rallyScreenViewModel,
                        darkThemeState = darkThemeState,
                        fontScaleState = fontScaleState,
                        changeLocale = ::changeLocale,
                        sharedPreferences = sharedPreferences,
                        recomposeNavbar = recomposeNavbar
                    )

                    val networkStatus by connectivityObserver.observe().collectAsState(
                        initial = ConnectivityObserver.Status.Available
                    )

                    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
                    val coroutineScope = rememberCoroutineScope()

                    LaunchedEffect(networkStatus) {
                        if (networkStatus == ConnectivityObserver.Status.Lost) {
                            coroutineScope.launch {
                                bottomSheetState.show()
                            }
                        } else if (networkStatus == ConnectivityObserver.Status.Available) {
                            coroutineScope.launch {
                                bottomSheetState.hide()
                            }
                        }
                    }

                    if (bottomSheetState.isVisible) {
                        ModalBottomSheet(
                            sheetState = bottomSheetState,
                            dragHandle = {},
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

private fun changeAppLocale(
    context: Context,
    localeString: String,
    recomposeNavbar: MutableState<Boolean>? = null
) {
    val locale = Locale(localeString)
    Locale.setDefault(locale)
    val config = Configuration(context.resources.configuration)
    config.setLocale(locale)
    context.createConfigurationContext(config)
    context.resources.updateConfiguration(config, context.resources.displayMetrics)

    // Save the selected language in SharedPreferences
    val sharedPreferences = context.getSharedPreferences("MyApp", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putString("SelectedLanguage", localeString)
    editor.apply()

    // Trigger navbar recomposition
    recomposeNavbar?.value = true
}