package com.canolabs.rallytransbetxi.ui.results

import com.canolabs.rallytransbetxi.data.models.responses.Result
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.canolabs.rallytransbetxi.R
import com.canolabs.rallytransbetxi.ui.theme.PaddingHuge
import com.canolabs.rallytransbetxi.ui.theme.PaddingLarge
import com.canolabs.rallytransbetxi.ui.theme.PaddingRegular
import com.canolabs.rallytransbetxi.ui.theme.ezraFamily
import com.canolabs.rallytransbetxi.ui.theme.robotoFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultsScreen(
    viewModel: ResultsScreenViewModel
) {
    val state by viewModel.state.collectAsState()
    val titles = listOf(R.string.global, R.string.stages)

    LaunchedEffect(Unit) {
        viewModel.fetchGlobalResults()
    }

    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        Text(
            text = stringResource(id = R.string.results),
            style = MaterialTheme.typography.displaySmall,
            fontFamily = ezraFamily,
            modifier = Modifier.padding(
                top = PaddingHuge,
                start = PaddingRegular,
                end = PaddingHuge,
                bottom = PaddingLarge
            )
        )

        SecondaryTabRow(selectedTabIndex = state.selectedTabIndex) {
            titles.forEachIndexed { index, title ->
                ResultsTab(
                    title = stringResource(id = title),
                    onClick = { viewModel.setSelectedTabIndex(index) },
                    selected = (index == state.selectedTabIndex),
                )
            }
        }

        if (state.selectedTabIndex == 0) {
            RacingCategorySegmentedButton(
                selectedTabIndex = state.selectedRacingCategory,
                onSelectedTabIndexChange = { viewModel.setSelectedRacingCategory(it) }
            )
            GlobalResultsList(results = state.results, isLoading = state.isLoading)
        } else {
            //StagesResultsList()
        }
    }
}

@Composable
fun GlobalResultsList(results: List<Result>, isLoading: Boolean) {
    if (isLoading) {
        ResultsCardShimmer()
    } else {
        val sortedResults = results.sortedBy { it.time }
        // We can not use LazyColumn here because we have set up
        // a vertical scrollable component in main function
        sortedResults.forEachIndexed { index, result ->
            ResultCard(result = result, position = index + 1)
        }
    }
}

@Composable
fun ResultsTab(
    title: String,
    onClick: () -> Unit,
    selected: Boolean,
) {
    Column(
        Modifier
            .clickable { onClick() }
            .padding(10.dp)
            .height(40.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            fontFamily = robotoFamily,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}
