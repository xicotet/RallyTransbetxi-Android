package com.canolabs.rallytransbetxi.ui.rally

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.canolabs.rallytransbetxi.R
import com.canolabs.rallytransbetxi.ui.theme.cardsElevation
import com.canolabs.rallytransbetxi.ui.theme.ezraFamily
import com.canolabs.rallytransbetxi.ui.theme.robotoFamily
import com.canolabs.rallytransbetxi.utils.Constants.Companion.DEFAULT_ACTIVITIES
import com.canolabs.rallytransbetxi.utils.DateTimeUtils
import java.util.Locale

@Composable
fun ActivityProgramSection(
    state: RallyScreenUIState,
    viewModel: RallyScreenViewModel
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(8.dp),
        shadowElevation = cardsElevation,
        onClick = { viewModel.toggleActivities() }
    ) {
        Column(
            modifier = Modifier
                .background(brush = getRallyScreenCardsGradient())
                .padding(16.dp)
        ) {
            Row (
                verticalAlignment = Alignment.CenterVertically,
            )  {
                Icon(
                    painter = painterResource(id = R.drawable.explore_outlined),
                    contentDescription = null,
                    modifier = Modifier.size(36.dp)
                )
                Text(
                    text = stringResource(id = R.string.activity_program).uppercase(Locale.ROOT),
                    style = MaterialTheme.typography.headlineMedium,
                    fontFamily = ezraFamily,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(start = 12.dp, top = 4.dp)
                        .weight(5f)
                )
                IconButton(onClick = { viewModel.toggleActivities() }) {
                    if (!state.areActivitiesCollapsed) {
                        Icon(
                            painter = painterResource(id = R.drawable.collapse_all),
                            modifier = Modifier
                                .size(48.dp)
                                .weight(1f)
                                .padding(end = 8.dp),
                            tint = MaterialTheme.colorScheme.onSurface,
                            contentDescription = null
                        )
                    } else {
                        Icon(
                            painter = painterResource(id = R.drawable.expand_all),
                            modifier = Modifier
                                .size(48.dp)
                                .weight(1f)
                                .padding(end = 8.dp),
                            tint = MaterialTheme.colorScheme.onSurface,
                            contentDescription = null
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (!state.isLoading) {
                AnimatedVisibility(visible = state.areActivitiesCollapsed.not()) {
                    Column {
                        if (state.activities.isNotEmpty()) {
                            Text(
                                text = DateTimeUtils.yearOfADateInSpanish(state.activities.first().date?.seconds ?: 0),
                                style = MaterialTheme.typography.titleMedium,
                                fontFamily = robotoFamily,
                                color = MaterialTheme.colorScheme.onSurface,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 8.dp, bottom = 4.dp)
                            )

                            HorizontalDivider(
                                modifier = Modifier.fillMaxWidth(),
                                thickness = 2.dp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        val activitiesToShow =
                            if (state.isShowAllActivitiesEnabled) state.activities
                            else state.activities.take(DEFAULT_ACTIVITIES)

                        activitiesToShow.forEach { activity ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight(),
                                onClick = {},
                                colors = CardColors(
                                    containerColor = Color.Transparent,
                                    contentColor = MaterialTheme.colorScheme.onSurface,
                                    disabledContainerColor = Color.Transparent,
                                    disabledContentColor = MaterialTheme.colorScheme.onSurface,
                                ),
                            ) {
                                Row(
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .height(IntrinsicSize.Min)
                                        .fillMaxWidth(),
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .align(Alignment.CenterVertically)
                                            .padding(end = 16.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = DateTimeUtils.dayOfADateInSpanish(
                                                activity.date?.seconds ?: 0
                                            ),
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontFamily = robotoFamily,
                                            color = MaterialTheme.colorScheme.onSurface,
                                        )

                                        Text(
                                            text = DateTimeUtils.monthOfADateInSpanish(
                                                activity.date?.seconds ?: 0
                                            ),
                                            style = MaterialTheme.typography.titleMedium,
                                            fontFamily = robotoFamily,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            modifier = Modifier.padding(vertical = 8.dp),
                                            textAlign = TextAlign.Start
                                        )
                                    }

                                    VerticalDivider(
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .width(4.dp)
                                            .padding(horizontal = 8.dp, vertical = 8.dp),
                                        thickness = 2.dp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )

                                    Column(
                                        modifier = Modifier
                                            .align(Alignment.CenterVertically)
                                            .padding(start = 16.dp),
                                    ) {
                                        if (activity.key != null) {
                                            Text(
                                                text = activity.key,
                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                    fontWeight = FontWeight.Bold
                                                ),
                                                fontFamily = robotoFamily,
                                                color = MaterialTheme.colorScheme.onSurface,
                                            )
                                        }
                                        Text(
                                            text = activity.activity,
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontFamily = robotoFamily,
                                            color = MaterialTheme.colorScheme.onSurface,
                                        )

                                        activity.hour?.let {
                                            Text(
                                                text = it,
                                                style = MaterialTheme.typography.titleMedium,
                                                fontFamily = robotoFamily,
                                                color = MaterialTheme.colorScheme.onSurface,
                                                modifier = Modifier.padding(vertical = 8.dp),
                                                textAlign = TextAlign.Start
                                            )
                                        }

                                        activity.place?.let {
                                            Text(
                                                text = it,
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontFamily = robotoFamily,
                                                color = MaterialTheme.colorScheme.onSurface,
                                                textAlign = TextAlign.Start
                                            )
                                        }
                                    }
                                }

                                // If it's the last element, don't print the divider
                                if (activity != activitiesToShow.last()) {
                                    HorizontalDivider(
                                        modifier = Modifier.fillMaxWidth(),
                                        thickness = 2.dp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }


                            }
                        }

                        if (state.activities.size > DEFAULT_ACTIVITIES) {
                            ClickableText(
                                text = AnnotatedString(
                                    if (state.isShowAllActivitiesEnabled) stringResource(id = R.string.show_less)
                                    else stringResource(id = R.string.show_all)
                                ),
                                onClick = { viewModel.toggleShowAllActivities() },
                                modifier = Modifier
                                    .padding(8.dp)
                                    .align(Alignment.CenterHorizontally),
                                style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.tertiary),
                            )
                        }
                    }
                }
            }
        }
    }
}