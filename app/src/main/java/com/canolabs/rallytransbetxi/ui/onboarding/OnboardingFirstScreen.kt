package com.canolabs.rallytransbetxi.ui.onboarding

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.canolabs.rallytransbetxi.ui.theme.onboardingBackground
import com.canolabs.rallytransbetxi.ui.theme.onboardingText
import com.canolabs.rallytransbetxi.ui.theme.robotoFamily

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingFirstScreen(
    pagerState: PagerState,
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
                painter = painterResource(id = R.drawable.motoret_1),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            )
            Text(
                text = stringResource(id = R.string.welcome),
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Start,
                color = onboardingText,
                fontFamily = antaFamily,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
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
                    .height(96.dp)
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.letsGetStarted),
                    style = MaterialTheme.typography.bodyMedium,
                    fontFamily = robotoFamily,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

}