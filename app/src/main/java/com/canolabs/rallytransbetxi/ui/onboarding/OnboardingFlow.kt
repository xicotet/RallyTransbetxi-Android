package com.canolabs.rallytransbetxi.ui.onboarding

import android.content.SharedPreferences
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.canolabs.rallytransbetxi.R
import com.canolabs.rallytransbetxi.domain.entities.Language
import com.canolabs.rallytransbetxi.ui.theme.onboardingBackground
import com.canolabs.rallytransbetxi.ui.theme.onboardingButtonDisabledBackground
import com.canolabs.rallytransbetxi.ui.theme.onboardingButtonDisabledContent
import com.canolabs.rallytransbetxi.ui.theme.robotoFamily
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingFlow(
    finishedOnboarding: MutableState<Boolean>,
    sharedPreferences: SharedPreferences,
    changeLanguage: (String) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { 3 })

    val selectedLanguage = remember { mutableStateOf<Language?>(null) }
    val language = sharedPreferences.getString("SelectedLanguage", Locale.getDefault().language)
    if (language != null) {
        selectedLanguage.value = Language.entries.find { it.getLanguageCode() == language }
    }

    Scaffold(
        bottomBar = {
            Column(
                modifier = Modifier
                    .wrapContentHeight()
                    .background(color = onboardingBackground),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row(
                    Modifier.padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    repeat(pagerState.pageCount) { iteration ->
                        val color =
                            if (pagerState.currentPage == iteration) MaterialTheme.colorScheme.primaryContainer
                            else MaterialTheme.colorScheme.primary
                        Box(
                            modifier = Modifier
                                .padding(2.dp)
                                .clip(CircleShape)
                                .background(color)
                                .size(16.dp)
                        )
                    }
                }

                ElevatedButton(
                    onClick = {
                        coroutineScope.launch {
                            if (pagerState.currentPage in 0..1) {
                                Log.d("OnboardingFlow", "Current page: ${pagerState.currentPage}. Chaning to page ${pagerState.currentPage + 1}")
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            } else {
                                sharedPreferences.edit().putBoolean("FinishedOnboarding", true).apply()
                                finishedOnboarding.value = true
                                selectedLanguage.value?.let { language ->
                                    changeLanguage(language.getLanguageCode())
                                }
                            }
                        }
                    },
                    colors = ButtonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        disabledContainerColor = onboardingButtonDisabledBackground,
                        disabledContentColor = onboardingButtonDisabledContent
                    ),
                    enabled = selectedLanguage.value != null || pagerState.currentPage != 2,
                    shape = RoundedCornerShape(32),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(96.dp)
                        .padding(16.dp)
                ) {
                    Text(
                        text = when (pagerState.currentPage) {
                            0 -> stringResource(id = R.string.letsGetStarted)
                            1 -> stringResource(id = R.string.next)
                            else -> stringResource(id = R.string.end)
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        fontFamily = robotoFamily,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = onboardingBackground,
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .padding(it)
            ) { page ->
                when (page) {
                    0 -> OnboardingFirstScreen()
                    1 -> OnboardingSecondScreen()
                    2 -> OnboardingThirdScreen(
                        selectedLanguage = selectedLanguage,
                    )
                }
            }
        }
    }
}