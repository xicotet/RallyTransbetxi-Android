package com.canolabs.rallytransbetxi.ui.rally.featured

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.canolabs.rallytransbetxi.data.models.responses.Restaurant
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RestaurantCards(
    restaurants: List<Restaurant>,
    onCardClick: (Restaurant) -> Unit,
    pagerState: PagerState,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        HorizontalPager(
            state = pagerState,
            pageSize = object : PageSize {
                override fun Density.calculateMainAxisPageSize(
                    availableSpace: Int,
                    pageSpacing: Int
                ): Int {
                    // Calculate the page size to show 80% of the available width
                    return ((availableSpace - 2 * pageSpacing) * 0.8f).toInt()
                }
            },
            verticalAlignment = Alignment.CenterVertically,
            contentPadding = PaddingValues(horizontal = 16.dp), // Add space around items
            pageSpacing = 16.dp // Set spacing between pages
        ) { page ->
            val restaurant = restaurants[page]
            val scaleFactor = if (page == pagerState.currentPage) 1f else 0.85f
            val pageHeight = 180.dp * scaleFactor // Scale the height of the page

            // Animate height transition smoothly
            val animatedHeight by animateDpAsState(targetValue = pageHeight, label = "")
            val alpha = if (restaurants[pagerState.currentPage] == restaurants[page]) 1f else 0.8f

            Card(
                onClick = {
                    if (restaurants[pagerState.currentPage] == restaurants[page]) {
                        onCardClick(restaurant)
                    } else {
                        coroutineScope.launch {
                            // Animate the scroll to the selected page
                            pagerState.animateScrollToPage(page)
                        }
                    }
                },
                modifier = Modifier
                    .height(animatedHeight)
                    .alpha(alpha),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                elevation = if (page == pagerState.currentPage) CardDefaults.cardElevation(defaultElevation = 4.dp)
                    else CardDefaults.cardElevation()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = restaurant.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 18.sp,
                        maxLines = 1,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                    )
                    Text(
                        text = "No description available. Lorem ipsum generator.",
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 14.sp,
                        maxLines = 2,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                    )
                    Text(
                        text = "Rating: ${"N/A"}",
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}