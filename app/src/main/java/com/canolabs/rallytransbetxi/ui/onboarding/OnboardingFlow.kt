package com.canolabs.rallytransbetxi.ui.onboarding

import android.content.SharedPreferences
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingFlow(
    pagerState: PagerState,
    finishedOnboarding: MutableState<Boolean>,
    sharedPreferences: SharedPreferences
) {
    val corroutineScope = rememberCoroutineScope()

    HorizontalPager(
        state = pagerState,
    ) { page ->
        when (page) {
            0 -> OnboardingFirstScreen(
                pagerState,
                onNextClick = {
                    corroutineScope.launch {
                        pagerState.animateScrollToPage(1)
                    }
                },
            )
            1 -> OnboardingSecondScreen(
                pagerState,
                onPreviousClick = {
                    corroutineScope.launch {
                        pagerState.animateScrollToPage(0)
                    }
                },
                onNextClick = {
                    corroutineScope.launch {
                        pagerState.animateScrollToPage(2)
                    }
                },
            )
            2 -> OnboardingThirdScreen(
                pagerState,
                onPreviousClick = {
                    corroutineScope.launch {
                        pagerState.animateScrollToPage(1)
                    }
                },
                onNextClick = {
                    corroutineScope.launch {
                        sharedPreferences.edit().putBoolean("FinishedOnboarding", true).apply()
                        finishedOnboarding.value = true
                    }
                }
            )
        }
    }
}