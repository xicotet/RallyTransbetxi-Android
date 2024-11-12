package com.canolabs.rallytransbetxi.ui.rally.homeSections

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.canolabs.rallytransbetxi.R
import com.canolabs.rallytransbetxi.ui.rally.RallyScreenUIState
import com.canolabs.rallytransbetxi.ui.rally.RallyScreenViewModel
import com.canolabs.rallytransbetxi.ui.rally.dialogs.getWarningContentByLanguage
import com.canolabs.rallytransbetxi.ui.rally.dialogs.getWarningTitleByLanguage
import com.canolabs.rallytransbetxi.ui.theme.cardsElevation
import com.canolabs.rallytransbetxi.ui.theme.ezraFamily
import com.canolabs.rallytransbetxi.ui.theme.robotoFamily
import com.canolabs.rallytransbetxi.utils.Constants
import com.canolabs.rallytransbetxi.utils.DateTimeUtils
import java.util.Locale

@Composable
fun WarningsSection(
    state: RallyScreenUIState,
    viewModel: RallyScreenViewModel,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(8.dp),
        shadowElevation = cardsElevation,
        onClick = {
            viewModel.toggleWarnings()
            viewModel.insertSettings()
        }
    ) {
        Column(
            modifier = Modifier
                .background(brush = getWarningSectionCardGradient())
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.notification_important),
                    contentDescription = null,
                    modifier = Modifier.size(36.dp)
                )
                Text(
                    text = stringResource(id = R.string.warnings).uppercase(Locale.ROOT),
                    style = MaterialTheme.typography.headlineMedium,
                    fontFamily = ezraFamily,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(start = 12.dp, top = 4.dp)
                        .weight(5f)
                )
                IconButton(
                    onClick = {
                        viewModel.toggleWarnings()
                        viewModel.insertSettings()
                    }
                ) {
                    if (!state.areWarningsCollapsed) {
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

            if (!state.isLoading) {
                AnimatedVisibility(visible = state.areWarningsCollapsed.not()) {
                    Column {
                        val warningsToShow =
                            if (state.isShowAllWarningsEnabled) state.warnings
                            else state.warnings.take(Constants.DEFAULT_WARNINGS)

                        warningsToShow.forEach { warning ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight(),
                                onClick = {
                                    viewModel.setWarningShownOnDialog(warning)
                                    viewModel.setIsDialogShowing(true)
                                },
                                colors = CardColors(
                                    containerColor = Color.Transparent,
                                    contentColor = MaterialTheme.colorScheme.onSurface,
                                    disabledContainerColor = Color.Transparent,
                                    disabledContentColor = MaterialTheme.colorScheme.onSurface,
                                ),
                            ) {
                                Column(
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .fillMaxWidth(),
                                ) {
                                    Text(
                                        text = getWarningTitleByLanguage(warning, state.language),
                                        style = MaterialTheme.typography.titleMedium,
                                        fontFamily = robotoFamily,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.padding(vertical = 8.dp),
                                        textAlign = TextAlign.Start
                                    )

                                    Row {
                                        Icon(
                                            imageVector = Icons.Default.DateRange,
                                            modifier = Modifier.padding(end = 4.dp),
                                            contentDescription = null
                                        )
                                        Text(
                                            text = DateTimeUtils.secondsToDate(
                                                seconds = warning.date?.seconds ?: 0,
                                                language = state.language?.getLanguageCode()
                                                    ?: "es",
                                                country = state.language?.getCountryCode() ?: "ES"
                                            ),
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontFamily = robotoFamily,
                                            color = MaterialTheme.colorScheme.onSurface,
                                        )
                                    }

                                    Text(
                                        text = getWarningContentByLanguage(warning, state.language),
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontFamily = robotoFamily,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.padding(vertical = 8.dp),
                                        textAlign = TextAlign.Start
                                    )
                                }
                            }
                        }

                        if (state.warnings.size > Constants.DEFAULT_WARNINGS) {
                            ClickableText(
                                text = AnnotatedString(
                                    if (state.isShowAllWarningsEnabled) stringResource(id = R.string.show_less)
                                    else stringResource(id = R.string.show_all_male)
                                ),
                                onClick = { viewModel.toggleShowAllWarnings() },
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

@Composable
fun getWarningSectionCardGradient(): Brush {
    return Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.55f),
            MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.85f)
        ),
        start = Offset(0f, 0f),
        end = Offset(1000f, 1000f)
    )
}