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
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.canolabs.rallytransbetxi.ui.miscellaneous.Shimmer
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
        val brush = when (type) {
            HomeSectionType.NEWS -> getBreakingNewsCardGradient()
            HomeSectionType.ACTIVITIES -> getActivityProgramCardGradient()
            HomeSectionType.WARNINGS -> getWarningSectionCardGradient()
        }

        Column(
            modifier = Modifier
                .background(brush = brush)
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