package com.canolabs.rallytransbetxi.ui.results

import android.util.Log
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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

    Log.d("ResultsScreen", "Results: ${state.results}")

    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
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
            GlobalResultsList(results = state.results)
        } else {
            //StagesResultsList()
        }
    }
}

@Composable
fun GlobalResultsList(results: List<Result>) {
    LazyColumn {
        items(results) { result ->
            GlobalResultItem(result = result)
        }
    }
}

@Composable
fun GlobalResultItem(result: Result) {
    Column {
        Text(
            text = result.team.name,
            style = MaterialTheme.typography.bodyLarge,
            fontFamily = robotoFamily,
            modifier = Modifier.padding(10.dp)
        )
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
