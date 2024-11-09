package com.canolabs.rallytransbetxi.ui.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.canolabs.rallytransbetxi.R
import com.canolabs.rallytransbetxi.ui.theme.onboardingCardBackground
import com.canolabs.rallytransbetxi.ui.theme.onboardingText
import com.canolabs.rallytransbetxi.ui.theme.cardsElevation
import com.canolabs.rallytransbetxi.ui.theme.antaFamily
import com.canolabs.rallytransbetxi.ui.theme.robotoFamily

data class SquareItem(
    val imageResId: Int,
    val title: Int
)

@Composable
fun OnboardingSecondScreen(
    shouldLoadBiggerImage: Boolean
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            Image(
                painter = painterResource(id = if (shouldLoadBiggerImage) R.drawable.motoret_2 else R.drawable.motoret_2_svg),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            )
            Text(
                text = stringResource(id = R.string.welcome_to_transbetxi),
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Start,
                color = onboardingText,
                fontFamily = antaFamily,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
            Text(
                text = stringResource(id = R.string.welcome_to_transbetxi_description),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Start,
                color = onboardingText,
                fontFamily = robotoFamily,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        val squareItems = listOf(
            SquareItem(R.drawable.sports_score, R.string.results),
            SquareItem(R.drawable.group_outlined, R.string.teams),
            SquareItem(R.drawable.map_outlined, R.string.stages_maps),
            SquareItem(R.drawable.directions_outlined, R.string.directions)
        )
        
        // Display items in rows of 2
        val rows = squareItems.chunked(2)
        items(rows) { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                content = {
                    rowItems.forEach { item ->
                        Card(
                            modifier = Modifier
                                .padding(vertical = 8.dp, horizontal = 16.dp)
                                .size(width = 150.dp, height = 100.dp),
                            colors = CardColors(
                                containerColor = onboardingCardBackground,
                                contentColor = onboardingText,
                                disabledContainerColor = onboardingCardBackground,
                                disabledContentColor = onboardingText,
                            ),
                            elevation = CardDefaults.cardElevation(cardsElevation)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Image(
                                    painter = painterResource(item.imageResId),
                                    contentDescription = null,
                                    modifier = Modifier.size(48.dp)
                                )
                                Text(
                                    text = stringResource(id = item.title),
                                    style = MaterialTheme.typography.bodyMedium,
                                    textAlign = TextAlign.Center,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                )
                            }
                        }
                    }
                    // Add a spacer to ensure items are evenly spaced
                    Spacer(modifier = Modifier.width(8.dp))
                }
            )
        }

        // Add a spacer at the end to push content up if it's scrollable
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}