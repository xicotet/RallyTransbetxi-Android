package com.canolabs.rallytransbetxi.ui.results

import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip

@Composable
fun ResultsCardShimmer() {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Shimmer { brush ->
                Box(
                    modifier = Modifier
                        .height(24.dp)
                        .width(24.dp)
                        .background(brush = brush)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp),
                horizontalAlignment = Alignment.Start,
            ) {
                Shimmer { brush ->
                    Box(
                        modifier = Modifier
                            .height(24.dp)
                            .fillMaxWidth(0.7f)
                            .background(brush = brush)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Shimmer { brush ->
                    Box(
                        modifier = Modifier
                            .height(24.dp)
                            .fillMaxWidth(0.5f)
                            .background(brush = brush)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Shimmer { brush ->
                    Box(
                        modifier = Modifier
                            .height(24.dp)
                            .fillMaxWidth(0.3f)
                            .background(brush = brush)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .height(96.dp)
                    .width(96.dp)
            ) {
                Shimmer { brush ->
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .fillMaxSize()
                            .background(brush = brush)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Shimmer { brush ->
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .fillMaxSize()
                            .background(brush = brush)
                    )
                }
            }
        }
    }
}