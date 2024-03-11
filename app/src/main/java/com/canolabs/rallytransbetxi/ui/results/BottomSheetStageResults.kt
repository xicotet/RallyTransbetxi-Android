package com.canolabs.rallytransbetxi.ui.results

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.canolabs.rallytransbetxi.R
import com.canolabs.rallytransbetxi.ui.theme.antaFamily
import com.canolabs.rallytransbetxi.ui.theme.robotoFamily
import com.canolabs.rallytransbetxi.utils.DateTimeUtils

@Composable
fun BottomSheetStageResults(
    state: ResultsScreenUIState,
    viewModel: ResultsScreenViewModel
) {
    Column(
        modifier = Modifier.padding(bottom = 32.dp),
    ) {
        Row(
            modifier = Modifier
                .height(IntrinsicSize.Min)
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Column of two texts
            Column(
                modifier = Modifier
                    .weight(0.8f)
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = state.stageSelected.startTime?.let {
                        DateTimeUtils.secondsToDateInSpanishAbbreviated(it.seconds)
                    }.toString(),
                    fontFamily = robotoFamily,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier
                        .padding(start = 8.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painterResource(id = R.drawable.schedule_filled),
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = DateTimeUtils.formatTimeFromSeconds(
                            state.stageSelected.startTime?.seconds ?: 0
                        ),
                        fontFamily = antaFamily,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }

            Column (
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = state.stageSelected.acronym,
                    fontFamily = robotoFamily,
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier
                        .padding(8.dp)
                )
                OutlinedButton(
                    shape = MaterialTheme.shapes.extraLarge,
                    border = BorderStroke(2.dp, MaterialTheme.colorScheme.secondary),
                    onClick = { /*TODO*/ }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.map_outlined),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = stringResource(id = R.string.location))
                }
            }
        }

        RacingCategorySegmentedButton(
            selectedTabIndex = state.selectedRacingCategory.getTabIndex(),
            onSelectedTabIndexChange = { viewModel.setSelectedRacingCategory(it) }
        )

        if (state.isBottomSheetLoading) {
            ResultsCardShimmer()
        } else {
            val sortedResultsByTime = state.stageResults.sortedBy { it.time }

            val filteredResultsByCategory = sortedResultsByTime.filter {
                it.team.category.name == stringResource(id = state.selectedRacingCategory.getName())
            }
            // We can not use LazyColumn here because we have set up
            // a vertical scrollable component in main function
            filteredResultsByCategory.forEachIndexed { index, result ->
                ResultCard(result = result, position = index + 1)
            }
        }
    }
}