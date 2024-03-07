package com.canolabs.rallytransbetxi.ui.rally

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.canolabs.rallytransbetxi.R
import com.canolabs.rallytransbetxi.ui.theme.cardsElevation
import com.canolabs.rallytransbetxi.ui.theme.ezraFamily

@Composable
fun RallyScreen() {
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        item {
            Image(
                painter = painterResource(id = R.drawable.transbetxi_header),
                contentDescription = null
            )
            CountdownTimer()
            BreakingNewsCard()
            ListOfChampionsCard()
            ActivityProgramCard()
            WhereToEatCard()
        }
    }
}

@Composable
fun BreakingNewsCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 16.dp),
        shape = RoundedCornerShape(8.dp),
        onClick = {},
        elevation = CardDefaults.cardElevation(cardsElevation)
    ) {
        val gradient = Brush.linearGradient(
            colors = listOf(
                MaterialTheme.colorScheme.surface,
                MaterialTheme.colorScheme.secondary.copy(alpha = 0.25f)
            ),
            start = Offset(0f, 0f), // Start at the top left corner
            end = Offset(1000f, 1000f)
        )

        Row(
            modifier = Modifier
                .background(brush = gradient)
                .padding(32.dp)
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.campaign_outlined),
                modifier = Modifier
                    .size(48.dp)
                    .weight(1f)
                    .padding(end = 8.dp),
                tint = MaterialTheme.colorScheme.onSurface,
                contentDescription = null
            )
            Text(
                text = stringResource(id = R.string.breaking_news),
                style = MaterialTheme.typography.headlineLarge,
                fontFamily = ezraFamily,
                maxLines = 1,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .weight(5f)
            )
            Icon(
                painter = painterResource(id = R.drawable.arrow_forward_ios),
                modifier = Modifier.align(Alignment.CenterVertically),
                contentDescription = null
            )
        }
    }
}

@Composable
fun ListOfChampionsCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 16.dp),
        shape = RoundedCornerShape(8.dp),
        onClick = {},
        elevation = CardDefaults.cardElevation(cardsElevation)
    ) {
        val gradient = Brush.linearGradient(
            colors = listOf(
                MaterialTheme.colorScheme.surface,
                MaterialTheme.colorScheme.secondary.copy(alpha = 0.25f)
            ),
            start = Offset(0f, 0f), // Start at the top left corner
            end = Offset(1000f, 1000f)
        )

        Row(
            modifier = Modifier
                .background(brush = gradient)
                .padding(32.dp)
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.trophy_outlined),
                modifier = Modifier
                    .size(48.dp)
                    .weight(1f)
                    .padding(end = 8.dp),
                tint = MaterialTheme.colorScheme.onSurface,
                contentDescription = null
            )
            Text(
                text = stringResource(id = R.string.list_of_champions),
                style = MaterialTheme.typography.headlineLarge,
                fontFamily = ezraFamily,
                maxLines = 1,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .weight(5f)
            )
            Icon(
                painter = painterResource(id = R.drawable.arrow_forward_ios),
                modifier = Modifier.align(Alignment.CenterVertically),
                contentDescription = null
            )
        }
    }
}

@Composable
fun ActivityProgramCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 16.dp),
        shape = RoundedCornerShape(8.dp),
        onClick = {},
        elevation = CardDefaults.cardElevation(cardsElevation)
    ) {
        val gradient = Brush.linearGradient(
            colors = listOf(
                MaterialTheme.colorScheme.surface,
                MaterialTheme.colorScheme.secondary.copy(alpha = 0.25f)
            ),
            start = Offset(0f, 0f), // Start at the top left corner
            end = Offset(1000f, 1000f)
        )

        Row(
            modifier = Modifier
                .background(brush = gradient)
                .padding(32.dp)
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.explore_outlined),
                modifier = Modifier
                    .size(48.dp)
                    .weight(1f)
                    .padding(end = 8.dp),
                tint = MaterialTheme.colorScheme.onSurface,
                contentDescription = null
            )
            Text(
                text = stringResource(id = R.string.activity_program),
                style = MaterialTheme.typography.headlineLarge,
                fontFamily = ezraFamily,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .weight(5f)
            )
            Icon(
                painter = painterResource(id = R.drawable.arrow_forward_ios),
                modifier = Modifier.align(Alignment.CenterVertically),
                contentDescription = null
            )
        }
    }
}

@Composable
fun WhereToEatCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 16.dp),
        shape = RoundedCornerShape(8.dp),
        onClick = {},
        elevation = CardDefaults.cardElevation(cardsElevation)
    ) {
        val gradient = Brush.linearGradient(
            colors = listOf(
                MaterialTheme.colorScheme.surface,
                MaterialTheme.colorScheme.secondary.copy(alpha = 0.25f)
            ),
            start = Offset(0f, 0f), // Start at the top left corner
            end = Offset(1000f, 1000f)
        )

        Row(
            modifier = Modifier
                .background(brush = gradient)
                .padding(32.dp)
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.restaurant_outlined),
                modifier = Modifier
                    .size(48.dp)
                    .weight(1f)
                    .padding(end = 8.dp),
                tint = MaterialTheme.colorScheme.onSurface,
                contentDescription = null
            )
            Text(
                text = stringResource(id = R.string.where_to_eat),
                style = MaterialTheme.typography.headlineLarge,
                fontFamily = ezraFamily,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .weight(5f)
            )
            Icon(
                painter = painterResource(id = R.drawable.arrow_forward_ios),
                modifier = Modifier.align(Alignment.CenterVertically),
                contentDescription = null
            )
        }
    }
}