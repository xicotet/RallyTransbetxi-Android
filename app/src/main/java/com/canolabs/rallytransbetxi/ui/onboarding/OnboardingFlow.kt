package com.canolabs.rallytransbetxi.ui.onboarding

import android.content.SharedPreferences
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.canolabs.rallytransbetxi.domain.entities.Language
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingFlow(
    pagerState: PagerState,
    finishedOnboarding: MutableState<Boolean>,
    sharedPreferences: SharedPreferences,
    changeLanguage: (String) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    val selectedLanguage = remember { mutableStateOf<Language?>(null) }
    val language = sharedPreferences.getString("SelectedLanguage", Locale.getDefault().language)
    if (language != null) {
        selectedLanguage.value = Language.entries.find { it.getLanguageCode() == language }
    }

    HorizontalPager(
        state = pagerState,
    ) { page ->
        when (page) {
            0 -> OnboardingFirstScreen(
                pagerState,
                onNextClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(1)
                    }
                },
            )
            1 -> OnboardingSecondScreen(
                pagerState,
                onPreviousClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(0)
                    }
                },
                onNextClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(2)
                    }
                },
            )
            2 -> OnboardingThirdScreen(
                pagerState,
                selectedLanguage = selectedLanguage,
                onPreviousClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(1)
                    }
                },
                onNextClick = {
                    coroutineScope.launch {
                        sharedPreferences.edit().putBoolean("FinishedOnboarding", true).apply()
                        finishedOnboarding.value = true
                        selectedLanguage.value?.let { language ->
                            changeLanguage(language.getLanguageCode())
                        }
                    }
                }
            )
        }
    }
}