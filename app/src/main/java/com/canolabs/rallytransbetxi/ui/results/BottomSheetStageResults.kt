package com.canolabs.rallytransbetxi.ui.results

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.canolabs.rallytransbetxi.R
import com.canolabs.rallytransbetxi.ui.theme.PaddingLarge
import com.canolabs.rallytransbetxi.ui.theme.PaddingMedium
import com.canolabs.rallytransbetxi.ui.theme.PaddingRegular
import com.canolabs.rallytransbetxi.ui.theme.robotoFamily

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
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column (
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = state.stageSelected.acronym + " - " + state.stageSelected.name,
                    fontFamily = robotoFamily,
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 16.dp)
                )
                if (state.isBottomSheetSearchBarVisible) {
                    TextField(
                        value = state.searchText,
                        onValueChange = viewModel::setSearchText,
                        modifier = Modifier
                            .padding(vertical = PaddingLarge, horizontal = PaddingMedium)
                            .fillMaxWidth()
                            .height(56.dp)
                            .border(
                                2.dp,
                                MaterialTheme.colorScheme.onSurface,
                                MaterialTheme.shapes.extraLarge
                            ),
                        colors = TextFieldDefaults.colors().copy(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            cursorColor = MaterialTheme.colorScheme.onSurface
                        ),
                        shape = MaterialTheme.shapes.small,
                        singleLine = true,
                        placeholder = { Text(stringResource(id = R.string.search)) },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.search),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = { viewModel.setIsBottomSheetSearchBarVisible(false) },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.close),
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    )
                } else {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        OutlinedButton(
                            shape = MaterialTheme.shapes.extraLarge,
                            colors = ButtonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary,
                                disabledContainerColor = MaterialTheme.colorScheme.primary,
                                disabledContentColor = MaterialTheme.colorScheme.primary
                            ),
                            border = BorderStroke(2.dp, MaterialTheme.colorScheme.scrim),
                            onClick = { /*TODO*/ }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.map_outlined),
                                contentDescription = null
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = stringResource(id = R.string.location))
                        }
                        IconButton(
                            onClick = { viewModel.setIsBottomSheetSearchBarVisible(true) },
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .padding(horizontal = PaddingRegular)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.search),
                                contentDescription = null,
                                modifier = Modifier.size(48.dp)
                            )
                        }
                    }
                }
            }
        }

        RacingCategorySegmentedButton(
            selectedRacingCategories = state.selectedRacingCategories,
            onSelectedTabIndexChange = { tabIndex ->
                if (state.selectedRacingCategories.any { it.getTabIndex() == tabIndex }) {
                    viewModel.removeSelectedRacingCategoryWithIndex(tabIndex)
                } else {
                    viewModel.addSelectedRacingCategoryWithIndex(tabIndex)
                }
            }
        )

        if (state.isBottomSheetLoading) {
            for (i in 0..3) {
                ResultsCardShimmer()
            }
        } else {
            val sortedResultsByTime = state.stageResults.sortedBy { it.time }

            val filteredResultsBySearchBar = if (state.isBottomSheetSearchBarVisible) {
                sortedResultsByTime.filter { result ->
                    result.team.driver.contains(state.searchText, ignoreCase = true) ||
                        result.team.codriver.contains(state.searchText, ignoreCase = true) ||
                        result.team.name.contains(state.searchText, ignoreCase = true) ||
                        result.time.contains(state.searchText, ignoreCase = true)
                }
            } else {
                sortedResultsByTime
            }

            val filteredResultsByCategory = filteredResultsBySearchBar.filter { result ->
                state.selectedRacingCategories.any { selectedCategory ->
                    result.team.category.name == stringResource(id = selectedCategory.getName())
                }
            }

            LazyColumn {
                items(filteredResultsByCategory) { result ->
                    ResultCard(result = result, position = filteredResultsByCategory.indexOf(result) + 1)
                }
            }
        }
    }
}