package com.canolabs.rallytransbetxi.ui.onboarding

import androidx.compose.runtime.Composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.canolabs.rallytransbetxi.R
import com.canolabs.rallytransbetxi.domain.entities.Language
import com.canolabs.rallytransbetxi.ui.theme.antaFamily
import com.canolabs.rallytransbetxi.ui.theme.cardsElevation
import com.canolabs.rallytransbetxi.ui.theme.onboardingCardBackground
import com.canolabs.rallytransbetxi.ui.theme.onboardingText


@Composable
fun OnboardingThirdScreen(
    selectedLanguage: MutableState<Language?>,
    shouldLoadBiggerImage: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        Image(
            painter = painterResource(id = if (shouldLoadBiggerImage) R.drawable.motoret_3 else R.drawable.motoret_3_svg),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        )
        Text(
            text = stringResource(id = R.string.pick_a_language),
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Start,
            color = onboardingText,
            fontFamily = antaFamily,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            content = {
                items(Language.entries.size) { index ->
                    val language = Language.entries[index]
                    Card(
                        modifier = Modifier
                            .size(100.dp)
                            .padding(8.dp)
                            .clickable {
                                // If it's already selected, deselect it
                                if (selectedLanguage.value == language) {
                                    selectedLanguage.value = null
                                    return@clickable
                                }
                                selectedLanguage.value = language
                            }
                            .border(
                                width = 2.dp,
                                color = if (selectedLanguage.value == language)
                                    MaterialTheme.colorScheme.secondary
                                else Color.Transparent,
                                shape = MaterialTheme.shapes.medium
                            ),
                        colors = CardDefaults.cardColors(
                            containerColor = onboardingCardBackground,
                            contentColor = onboardingText,
                            disabledContainerColor = onboardingCardBackground,
                            disabledContentColor = onboardingText,
                        ),
                        elevation = CardDefaults.cardElevation(cardsElevation)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Image(
                                painter = painterResource(language.flagResource()),
                                contentDescription = null,
                                modifier = Modifier.size(48.dp)
                            )
                        }
                    }
                }
            }
        )
    }
}