package com.canolabs.rallytransbetxi.ui.rally

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.canolabs.rallytransbetxi.R
import com.canolabs.rallytransbetxi.ui.miscellaneous.Shimmer
import com.canolabs.rallytransbetxi.ui.theme.cardsElevation
import com.canolabs.rallytransbetxi.ui.theme.ezraFamily
import com.canolabs.rallytransbetxi.ui.theme.robotoFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HallOfFameScreen(
    viewModel: RallyScreenViewModel,
    onBackClick: () -> Unit
) {
    val state = viewModel.state.collectAsState()
    Log.d("HallOfFameServiceImpl", "Enters HallOfFameScreen.kt")
    LaunchedEffect(Unit) {
        Log.d("HallOfFameServiceImpl", "Enters HallOfFameScreen.kt LaunchedEffect")
        viewModel.fetchHallOfFame()
    }

    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    scrolledContainerColor = MaterialTheme.colorScheme.surface,
                ),
                title = {
                    Text(
                        stringResource(id = R.string.hall_of_fame),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = ezraFamily
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick,
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            modifier = Modifier.size(32.dp),
                            contentDescription = "Localized description"
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(it)
                .padding(16.dp),
            shape = RoundedCornerShape(8.dp),
            shadowElevation = cardsElevation,
        ) {

            Spacer(modifier = Modifier.height(8.dp))

            if (state.value.isHallOfFameLoading) {
                Shimmer { brush ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .padding(16.dp)
                            .background(brush = brush)
                    )
                }
            } else {
                Column(
                    modifier = Modifier
                        .verticalScroll(scrollState)
                        .background(brush = getRallyScreenCardsGradient())
                        .padding(16.dp)
                ) {
                    val hallOfFameToShow = state.value.hallOfFame

                    hallOfFameToShow.forEach { hallOfFame ->
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
                                        text = hallOfFame.year,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontFamily = robotoFamily,
                                        color = MaterialTheme.colorScheme.onSurface,
                                    )
                                }

                                VerticalDivider(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .width(4.dp)
                                        .padding(horizontal = 8.dp, vertical = 2.dp),
                                    thickness = 2.dp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                Column(
                                    modifier = Modifier
                                        .align(Alignment.CenterVertically)
                                        .padding(start = 16.dp),
                                ) {
                                    Text(
                                        text = hallOfFame.team,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = FontWeight.Bold
                                        ),
                                        fontFamily = robotoFamily,
                                        color = MaterialTheme.colorScheme.onSurface,
                                    )
                                    Text(
                                        text = hallOfFame.driverCodriver,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontFamily = robotoFamily,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }

                            // If it's the last element, don't print the divider
                            if (hallOfFame != hallOfFameToShow.last()) {
                                HorizontalDivider(
                                    modifier = Modifier.fillMaxWidth(),
                                    thickness = 2.dp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }

        }
    }
}