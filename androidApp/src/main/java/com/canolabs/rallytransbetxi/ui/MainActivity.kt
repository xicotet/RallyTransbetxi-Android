package com.canolabs.rallytransbetxi.ui

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.canolabs.rallytransbetxi.ui.maps.MapsScreenViewModel
import com.canolabs.rallytransbetxi.ui.navigation.Navigation
import com.canolabs.rallytransbetxi.ui.rally.RallyScreenViewModel
import com.canolabs.rallytransbetxi.ui.results.ResultsScreenViewModel
import com.canolabs.rallytransbetxi.ui.stages.StagesScreenViewModel
import com.canolabs.rallytransbetxi.ui.teams.TeamsScreenViewModel
import com.canolabs.rallytransbetxi.ui.theme.RallyTransbetxiTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import com.canolabs.rallytransbetxi.shared.App
import com.canolabs.rallytransbetxi.ui.miscellaneous.network.BottomSheetConnectivityLost
import com.canolabs.rallytransbetxi.ui.miscellaneous.network.ConnectivityObserver
import com.canolabs.rallytransbetxi.ui.miscellaneous.network.NetworkConnectivityObserver
import com.canolabs.rallytransbetxi.utils.Constants.Companion.SPLASH_SCREEN_DURATION
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import java.util.Locale

class MainActivity : ComponentActivity() {

    private lateinit var connectivityObserver: ConnectivityObserver

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*val splashScreen = installSplashScreen()

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

        connectivityObserver = NetworkConnectivityObserver(applicationContext)*/

        setContent {
            //EnableTransparentStatusBar()


            val darkThemeState = remember { mutableStateOf(false) }
            val fontScaleState = remember { mutableFloatStateOf(1f) }
            val recomposeNavbar = remember { mutableStateOf(false) }

            RallyTransbetxiTheme(
                darkTheme = darkThemeState,
                fontScale = fontScaleState
            ) {
                App()
            }

            /*RallyTransbetxiTheme(
                darkTheme = darkThemeState,
                fontScale = fontScaleState
            ) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val stagesScreenViewModel: StagesScreenViewModel = koinViewModel<StagesScreenViewModel>()
                    val teamsScreenViewModel: TeamsScreenViewModel = koinViewModel<TeamsScreenViewModel>()
                    val resultsScreenViewModel: ResultsScreenViewModel = koinViewModel<ResultsScreenViewModel>()
                    val mapsScreenViewModel: MapsScreenViewModel = koinViewModel<MapsScreenViewModel>()
                    val rallyScreenViewModel: RallyScreenViewModel = koinViewModel<RallyScreenViewModel>()

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
                }*/
        }
    }
}

/*@Composable
fun EnableTransparentStatusBar() {
    val view = LocalView.current
    val darkMode = isSystemInDarkTheme()
    if (!view.isInEditMode) {
        val window = (view.context as ComponentActivity).window
        window.statusBarColor = Color.Transparent.toArgb()
        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkMode
    }
}*/


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