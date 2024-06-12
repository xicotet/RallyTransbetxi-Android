package com.canolabs.rallytransbetxi.ui.onboarding

import androidx.compose.runtime.Composable

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.canolabs.rallytransbetxi.R
import com.canolabs.rallytransbetxi.ui.theme.antaFamily
import com.canolabs.rallytransbetxi.ui.theme.cardsElevation
import com.canolabs.rallytransbetxi.ui.theme.onboardingBackground
import com.canolabs.rallytransbetxi.ui.theme.onboardingCardBackground
import com.canolabs.rallytransbetxi.ui.theme.onboardingText
import com.canolabs.rallytransbetxi.ui.theme.robotoFamily

data class SquareItem(
    val imageResId: Int,
    val title: String
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingSecondScreen(
    pagerState: PagerState,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit
) {
    Surface(
        color = onboardingBackground,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            Image(
                painter = painterResource(id = R.drawable.motoret_2),
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
                    .padding(horizontal = 16.dp)
            )

            val squareItems = listOf(
                SquareItem(R.drawable.sports_score, stringResource(R.string.live_results)),
                SquareItem(R.drawable.group_outlined, stringResource(R.string.teams)),
                SquareItem(R.drawable.map_outlined, stringResource(R.string.stages_maps)),
                SquareItem(R.drawable.directions_outlined, stringResource(R.string.directions))
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                content = {
                    items(squareItems.size) { index ->
                        val item = squareItems[index]
                        Card(
                            modifier = Modifier
                                .size(100.dp)
                                .padding(8.dp),
                            colors = CardColors(
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
                                    painter = painterResource(item.imageResId),
                                    contentDescription = null,
                                    modifier = Modifier.size(48.dp)
                                )
                                Text(
                                    text = item.title,
                                    style = MaterialTheme.typography.bodyMedium,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.weight(1f))

            Row(
                Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 8.dp),
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

            Row(
                modifier = Modifier.fillMaxWidth().height(96.dp)
                    .padding(16.dp)
            ) {
                IconButton(
                    onClick = onPreviousClick,
                    colors = IconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        disabledContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        disabledContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    modifier = Modifier
                        .clip(CircleShape)
                        .fillMaxHeight()
                        .width(96.dp)
                        .padding(end = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null
                    )
                }
                ElevatedButton(
                    onClick = onNextClick,
                    colors = ButtonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        disabledContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        disabledContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    modifier = Modifier
                        .clip(RectangleShape)
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(start = 8.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.next),
                        style = MaterialTheme.typography.bodyMedium,
                        fontFamily = robotoFamily,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

        }
    }

}