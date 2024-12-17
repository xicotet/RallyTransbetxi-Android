package com.canolabs.rallytransbetxi.ui.rally.featured

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.canolabs.rallytransbetxi.ui.miscellaneous.Shimmer

@Composable
fun RestaurantCardShimmer(modifier: Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth(0.8f)
            .height(300.dp),
    ) {
        Card(
            onClick = {},
            modifier = Modifier
                .fillMaxSize()
                .alpha(1f),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(32.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(90.dp)
                        .padding(bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy((-30).dp) // Overlap images
                ) {
                    repeat(4) {
                        Shimmer { brush ->
                            Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(CircleShape)
                                    .background(brush)
                            )
                        }
                    }
                }

                // Shimmer for the text fields (restaurant name, rating, etc.)
                Shimmer { brush ->
                    Box(
                        modifier = Modifier
                            .height(18.dp)
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .background(brush)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Shimmer { brush ->
                    Box(
                        modifier = Modifier
                            .height(18.dp)
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .background(brush)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Shimmer for buttons (e.g., Google Maps button, Call button)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Button(
                        onClick = {},
                        modifier = Modifier
                            .weight(2f)
                            .clip(RoundedCornerShape(50)),
                        contentPadding = PaddingValues(4.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Shimmer { brush ->
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(brush)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {},
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .clip(RoundedCornerShape(50)),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Shimmer { brush ->
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .background(brush)
                            )
                        }
                    }
                }
            }
        }
    }
}