package com.canolabs.rallytransbetxi.ui

import android.app.LocaleManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.LocaleList
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.core.os.LocaleListCompat
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var viewModelFactory: MainActivityViewModelFactory
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            val darkThemeState = remember { mutableStateOf(false) }

            RallyTransbetxiTheme(
                darkTheme = darkThemeState
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

                    Navigation(
                        stagesScreenViewModel = stagesScreenViewModel,
                        teamsScreenViewModel = teamsScreenViewModel,
                        resultsScreenViewModel = resultsScreenViewModel,
                        mapsScreenViewModel = mapsScreenViewModel,
                        rallyScreenViewModel = rallyScreenViewModel,
                        darkThemeState = darkThemeState,
                        changeLocale = ::changeLocale
                    )
                }
            }
        }
    }
}

fun changeLocale(context: Context, localeString: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        context.getSystemService(LocaleManager::class.java)
            .applicationLocales = LocaleList.forLanguageTags(localeString)
    } else {
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(localeString))
    }
}