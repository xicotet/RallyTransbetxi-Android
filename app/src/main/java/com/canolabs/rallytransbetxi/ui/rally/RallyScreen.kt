package com.canolabs.rallytransbetxi.ui.rally

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.canolabs.rallytransbetxi.R
import com.canolabs.rallytransbetxi.ui.theme.ezraFamily
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RallyScreen(
    viewModel: RallyScreenViewModel
) {
    val state by viewModel.state.collectAsState()
    val bottomSheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.fetchNews()
        viewModel.fetchActivities()
    }

    LazyColumn(
        verticalArrangement = Arrangement.Center
    ) {
        item {
            Image(
                painter = painterResource(id = R.drawable.transbetxi_header),
                contentDescription = null
            )

            CountdownTimer()

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(id = R.string.featured_section).uppercase(Locale.ROOT),
                style = MaterialTheme.typography.headlineMedium,
                fontFamily = ezraFamily,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = 38.dp),
                textAlign = TextAlign.Start
            )

            FeaturedSection(
                viewModel = viewModel
            )

            BreakingNewsSection(
                state = state,
                viewModel = viewModel
            )

            ActivityProgramSection(
                state = state,
                viewModel = viewModel
            )

            if (state.isSettingsBottomSheetVisible) {
                ModalBottomSheet(
                    sheetState = bottomSheetState,
                    onDismissRequest = {
                        coroutineScope.launch {
                            viewModel.setIsSettingsBottomSheetVisible(false)
                            bottomSheetState.hide()
                        }
                    },
                ) {
                    BottomSheetAppSettings(
                    )
                }
            }
        }
    }
}

@Composable
fun getRallyScreenCardsGradient(): Brush {
    return Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colorScheme.surface,
            MaterialTheme.colorScheme.secondary.copy(alpha = 0.25f)
        ),
        start = Offset(0f, 0f), // Start at the top left corner
        end = Offset(1000f, 1000f)
    )
}