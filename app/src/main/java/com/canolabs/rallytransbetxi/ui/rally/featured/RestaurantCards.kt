package com.canolabs.rallytransbetxi.ui.rally.featured

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.canolabs.rallytransbetxi.data.models.responses.Restaurant

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RestaurantCards(
    restaurants: List<Restaurant>,
    onCardClick: (Restaurant) -> Unit,
    onPageChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(pageCount = { restaurants.size })

    // Notify the current page whenever it changes
    LaunchedEffect(pagerState.currentPage) {
        onPageChange(pagerState.currentPage)
    }

    val fadingEdgeBrush = Brush.horizontalGradient(
        colorStops = arrayOf(
            0f to Color.Black.copy(alpha = 1f),
            0.9f to Color.Black.copy(alpha = 1f), // Maintain fully opaque for most of the content
            1f to Color.Transparent  // Fade back to transparent
        )
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .fadingEdge(fadingEdgeBrush) // Apply fade effect to edges
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

            Card(
                onClick = { onCardClick(restaurant) },
                modifier = Modifier
                    .height(animatedHeight), // Apply dynamic height based on scale
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
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

fun Modifier.fadingEdge(brush: Brush): Modifier = this
    .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
    .drawWithContent {
        drawContent()
        drawRect(brush = brush, blendMode = BlendMode.DstIn)
    }