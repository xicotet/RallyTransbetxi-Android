package com.canolabs.rallytransbetxi.ui.rally.homeSections

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.canolabs.rallytransbetxi.ui.miscellaneous.Shimmer
import com.canolabs.rallytransbetxi.ui.rally.getRallyScreenCardsGradient
import com.canolabs.rallytransbetxi.ui.theme.cardsElevation

@Composable
fun HomeSectionShimmer(type: HomeSectionType) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(8.dp),
        shadowElevation = cardsElevation,
    ) {
        val background = when (type) {
            HomeSectionType.WARNINGS -> MaterialTheme.colorScheme.errorContainer
            else -> null
        }

        val brush = when (type) {
            HomeSectionType.NEWS -> Brush.linearGradient(
                colors = listOf(
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                    MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f)
                ),
                start = Offset(0f, 0f),
                end = Offset(1000f, 1000f)
            )
            HomeSectionType.ACTIVITIES -> getRallyScreenCardsGradient()
            else -> null
        }

        val modifier = if (background != null) {
            Modifier.background(color = background)
        } else {
            Modifier.background(brush = brush!!)
        }

        Column(
            modifier = modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            // Icon and Header Shimmer
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Shimmer { brush ->
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(brush, shape = CircleShape)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Shimmer { brush ->
                    Box(
                        modifier = Modifier
                            .height(30.dp)
                            .align(Alignment.CenterVertically)
                            .fillMaxWidth(0.6f)
                            .background(brush, shape = RoundedCornerShape(4.dp))
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Shimmer { brush ->
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .align(Alignment.CenterVertically)
                            .background(brush, shape = RoundedCornerShape(4.dp))
                    )
                }

            }
        }
    }
}