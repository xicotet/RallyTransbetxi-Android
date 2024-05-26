package com.canolabs.rallytransbetxi.ui.rally

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.canolabs.rallytransbetxi.R
import com.canolabs.rallytransbetxi.ui.theme.ezraFamily
import java.util.Locale

@Composable
fun RallyScreen(
    viewModel: RallyScreenViewModel
) {
    val state by viewModel.state.collectAsState()

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

            FeaturedSection()

            BreakingNewsSection(
                state = state,
                viewModel = viewModel
            )

            ActivityProgramSection(
                state = state,
                viewModel = viewModel
            )
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