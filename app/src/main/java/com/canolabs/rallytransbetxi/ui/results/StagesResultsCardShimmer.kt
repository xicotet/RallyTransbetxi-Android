package com.canolabs.rallytransbetxi.ui.results

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.canolabs.rallytransbetxi.ui.miscellaneous.Shimmer

@Composable
fun StagesResultsCardShimmer() {
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
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1.2f)
                    .padding(start = 16.dp),
                horizontalAlignment = Alignment.Start,
            ) {
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
                            .fillMaxWidth(0.5f)
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
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .weight(1.3f)
                    .height(96.dp)
                    .width(96.dp)
            ) {
                Shimmer { brush ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(brush = brush)
                    )
                }
            }
        }
    }
}