package com.canolabs.rallytransbetxi.ui.results

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.canolabs.rallytransbetxi.R
import com.canolabs.rallytransbetxi.ui.maps.MapsScreenUIState
import com.canolabs.rallytransbetxi.ui.miscellaneous.removeDiacriticalMarks
import com.canolabs.rallytransbetxi.ui.navigation.Screens
import com.canolabs.rallytransbetxi.ui.theme.PaddingMedium
import com.canolabs.rallytransbetxi.ui.theme.PaddingRegular
import com.canolabs.rallytransbetxi.ui.theme.PaddingSmall
import com.canolabs.rallytransbetxi.ui.theme.robotoFamily
import com.canolabs.rallytransbetxi.utils.DateTimeUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetStageResults(
    resultsState: ResultsScreenUIState,
    mapsState: MapsScreenUIState? = null,
    viewModel: ResultsScreenViewModel,
    bottomSheetState: SheetState,
    isComingFromMaps: Boolean = false,
    navController: NavController
) {
    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val isScrollToTopVisible by remember {
        derivedStateOf {
            lazyListState.canScrollBackward
        }
    }

    val isRaceProgressStatusBarVisible = remember { mutableStateOf(false) }

    // Create the modifier with the conditional background for the race status icon
    val backgroundModifier = if (isRaceProgressStatusBarVisible.value) {
        Modifier.background(
            brush = Brush.radialGradient(
                colors = listOf(
                    MaterialTheme.colorScheme.primary,
                    MaterialTheme.colorScheme.secondary
                )
            ),
            shape = RoundedCornerShape(24.dp)
        )
    } else {
        Modifier
    }

    val raceProgressTextVerticalScroll = rememberScrollState()
    LaunchedEffect(isRaceProgressStatusBarVisible.value) {
        // If text is quite large, start automatic scrolling
        if (isRaceProgressStatusBarVisible.value) {
            delay(1000)
            raceProgressTextVerticalScroll.animateScrollTo(
                raceProgressTextVerticalScroll.maxValue,
                animationSpec = tween(3000)
            )
        }
    }

    Box(
        Modifier
            .fillMaxSize()
    ) {
        LazyColumn(
            state = lazyListState,
            modifier = Modifier
                .padding(bottom = 32.dp)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .height(IntrinsicSize.Min)
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = if (isComingFromMaps) mapsState?.stage?.acronym + " - " + mapsState?.stage?.name
                            else resultsState.stageSelected.acronym + " - " + resultsState.stageSelected.name,
                            fontFamily = robotoFamily,
                            style = MaterialTheme.typography.headlineMedium,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier
                                .padding(start = 8.dp, end = 8.dp, bottom = 16.dp)
                        )
                        AnimatedVisibility(
                            visible = resultsState.isBottomSheetSearchBarVisible,
                            enter = slideInHorizontally(initialOffsetX = { it }) + fadeIn(animationSpec = tween(durationMillis = 300)),
                        ) {
                            if (resultsState.isBottomSheetSearchBarVisible) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    TextField(
                                        value = resultsState.searchText,
                                        onValueChange = viewModel::setSearchText,
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(start = PaddingSmall, end = PaddingSmall)
                                            .height(56.dp)
                                            .clip(RoundedCornerShape(32))
                                            .border(
                                                2.dp,
                                                MaterialTheme.colorScheme.onSurface,
                                                RoundedCornerShape(32)
                                            ),
                                        colors = TextFieldDefaults.colors().copy(
                                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                                            focusedIndicatorColor = Color.Transparent,
                                            unfocusedIndicatorColor = Color.Transparent,
                                            cursorColor = MaterialTheme.colorScheme.onSurface
                                        ),
                                        shape = RoundedCornerShape(32),
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
                                            AnimatedVisibility(visible = resultsState.searchText.isNotEmpty()) {
                                                IconButton(
                                                    onClick = { viewModel.setSearchText("") },
                                                    modifier = Modifier.size(24.dp),
                                                ) {
                                                    Icon(
                                                        painter = painterResource(id = R.drawable.close),
                                                        contentDescription = null,
                                                        modifier = Modifier.size(24.dp)
                                                    )
                                                }
                                            }
                                        }
                                    )
                                    IconButton(
                                        onClick = { viewModel.setIsBottomSheetSearchBarVisible(false) },
                                        modifier = Modifier
                                            .padding(end = PaddingSmall)
                                            .size(56.dp)
                                            .border(
                                                width = 2.dp,
                                                color = MaterialTheme.colorScheme.onSurface,
                                                shape = RoundedCornerShape(32)
                                            ),
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.close),
                                            contentDescription = null,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                }
                            }
                        }

                        AnimatedVisibility(
                            visible = !resultsState.isBottomSheetSearchBarVisible,
                            enter = slideInHorizontally(initialOffsetX = { -it }) + fadeIn(animationSpec = tween(durationMillis = 300)),
                        ) {
                            if (!resultsState.isBottomSheetSearchBarVisible) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    IconButton(
                                        onClick = {
                                            isRaceProgressStatusBarVisible.value = !isRaceProgressStatusBarVisible.value
                                            if (bottomSheetState.currentValue == SheetValue.PartiallyExpanded && isRaceProgressStatusBarVisible.value) {
                                                coroutineScope.launch {
                                                    bottomSheetState.expand()
                                                }
                                            }
                                        },
                                        modifier = Modifier
                                            .align(Alignment.CenterVertically)
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.circle_notifications),
                                            contentDescription = null,
                                            tint = if (isRaceProgressStatusBarVisible.value) MaterialTheme.colorScheme.tertiaryContainer
                                            else LocalContentColor.current,
                                            modifier = backgroundModifier.size(48.dp)
                                        )
                                    }

                                    if (!isComingFromMaps) {
                                        OutlinedButton(
                                            shape = MaterialTheme.shapes.extraLarge,
                                            colors = ButtonColors(
                                                containerColor = MaterialTheme.colorScheme.primary,
                                                contentColor = MaterialTheme.colorScheme.onPrimary,
                                                disabledContainerColor = MaterialTheme.colorScheme.primary,
                                                disabledContentColor = MaterialTheme.colorScheme.primary
                                            ),
                                            modifier = Modifier
                                                .align(Alignment.CenterVertically)
                                                .padding(start = PaddingSmall),
                                            border = BorderStroke(
                                                2.dp,
                                                MaterialTheme.colorScheme.scrim
                                            ),
                                            onClick = {
                                                val fastAction: String? = null
                                                navController.navigate("${Screens.Maps.route}/${resultsState.stageSelected.acronym}/${fastAction}")
                                            }
                                        ) {
                                            Icon(
                                                painter = painterResource(id = R.drawable.map_outlined),
                                                contentDescription = null
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = stringResource(id = R.string.map),
                                                fontFamily = robotoFamily,
                                            )
                                        }
                                    }

                                    IconButton(
                                        onClick = { viewModel.setIsBottomSheetSearchBarVisible(true) },
                                        modifier = Modifier
                                            .align(Alignment.CenterVertically)
                                            .padding(start = PaddingSmall)
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
                }
            }

            item {
                RacingCategorySegmentedButton(
                    selectedRacingCategories = resultsState.selectedRacingCategories,
                    onSelectedTabIndexChange = { tabIndex ->
                        if (resultsState.selectedRacingCategories.any { it.getTabIndex() == tabIndex }) {
                            viewModel.removeSelectedRacingCategoryWithIndex(tabIndex)
                        } else {
                            viewModel.addSelectedRacingCategoryWithIndex(tabIndex)
                        }
                    }
                )

                if (resultsState.isBottomSheetLoading) {
                    for (i in 0..3) {
                        ResultsCardShimmer()
                    }
                } else {
                    val filteredResultsByCategory = resultsState.stageResults.filter { result ->
                        resultsState.selectedRacingCategories.any { selectedCategory ->
                            result.team.category.name == selectedCategory.getApiName()
                        }
                    }

                    val sortedResultsByTime = filteredResultsByCategory.sortedBy { it.time }

                    val filteredResultsBySearchBar =
                        if (resultsState.isBottomSheetSearchBarVisible) {
                            sortedResultsByTime.filter { result ->
                                result.team.driver.removeDiacriticalMarks().contains(
                                    resultsState.searchText.removeDiacriticalMarks(),
                                    ignoreCase = true
                                ) ||
                                    result.team.codriver.removeDiacriticalMarks().contains(
                                        resultsState.searchText.removeDiacriticalMarks(),
                                        ignoreCase = true
                                    ) ||
                                    result.team.name.removeDiacriticalMarks().contains(
                                        resultsState.searchText.removeDiacriticalMarks(),
                                        ignoreCase = true
                                    ) ||
                                    result.time.removeDiacriticalMarks().contains(
                                        resultsState.searchText.removeDiacriticalMarks(),
                                        ignoreCase = true
                                    ) ||
                                    result.team.number.removeDiacriticalMarks().contains(
                                        resultsState.searchText.removeDiacriticalMarks(),
                                        ignoreCase = true
                                    )
                            }
                        } else {
                            sortedResultsByTime
                        }

                    if (resultsState.stageResults.isEmpty()) {
                        // Show a placeholder indicating when the results will be available
                        val (startTime, languageCode, countryCode) = if (isComingFromMaps) {
                            Triple(
                                mapsState?.stage?.startTime?.seconds ?: 0,
                                mapsState?.language?.getLanguageCode() ?: "es",
                                mapsState?.language?.getCountryCode() ?: "ES"
                            )
                        } else {
                            Triple(
                                resultsState.stageSelected.startTime?.seconds ?: 0,
                                resultsState.language?.getLanguageCode() ?: "es",
                                resultsState.language?.getCountryCode() ?: "ES"
                            )
                        }

                        val startDate = DateTimeUtils.secondsToDate(
                            seconds = startTime,
                            language = languageCode,
                            country = countryCode
                        )
                        val startingHour = DateTimeUtils.formatTimeFromSeconds(startTime)

                        Text(
                            text = stringResource(id = R.string.results_available) + ' ' +
                                startDate + ' ' + stringResource(id = R.string.at_hour) + ' ' +
                                startingHour + '.',
                            style = MaterialTheme.typography.titleMedium,
                            fontFamily = robotoFamily,
                            modifier = Modifier.padding(vertical = 16.dp, horizontal = 24.dp)
                        )
                    }

                    if (filteredResultsBySearchBar.isEmpty() && resultsState.stageResults.isNotEmpty()) {
                        Text(
                            text = stringResource(id = R.string.results_are_hidden),
                            style = MaterialTheme.typography.titleMedium,
                            fontFamily = robotoFamily,
                            modifier = Modifier.padding(vertical = 16.dp, horizontal = 24.dp)
                        )
                    }

                    Column {
                        filteredResultsBySearchBar.forEach { result ->
                            key(result.team.number) {
                                ResultCard(
                                    result = result,
                                    position = sortedResultsByTime.indexOf(result) + 1,
                                    onClick = {
                                        navController.navigate(
                                            "${Screens.TeamDetail.route}/${result.team.number}"
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = isRaceProgressStatusBarVisible.value,
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 64.dp),
            enter = fadeIn(animationSpec = tween(durationMillis = 600)) + slideInVertically(
                initialOffsetY = { it }),
            exit = fadeOut(animationSpec = tween(durationMillis = 600)) + slideOutVertically(
                targetOffsetY = { it })
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .padding(PaddingRegular)
                    .background(
                        MaterialTheme.colorScheme.tertiaryContainer,
                        RoundedCornerShape(8.dp)
                    )
                    .padding(PaddingMedium),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Vertical scrolling text
                Text(
                    text = "Race Progress Status: Results are confirmed and finally!",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(raceProgressTextVerticalScroll) // Vertical scrolling
                        .padding(end = 16.dp) // Add padding to avoid text touching the edge
                )

                // Close button (cross icon) centered vertically
                IconButton(
                    onClick = { isRaceProgressStatusBarVisible.value = false },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }
            }
        }

        if (isScrollToTopVisible) {
            FloatingActionButton(
                onClick = {
                    coroutineScope.launch {
                        lazyListState.animateScrollToItem(0)
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
                    .padding(bottom = 32.dp), // Ensure it's not part of the scrollable content
                content = {
                    Icon(
                        painter = painterResource(id = R.drawable.arrow_upward),
                        contentDescription = "Scroll to top",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            )
        }
    }
}