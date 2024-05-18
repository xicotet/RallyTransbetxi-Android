package com.canolabs.rallytransbetxi.ui.teams

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.canolabs.rallytransbetxi.ui.miscellaneous.Shimmer
import com.canolabs.rallytransbetxi.ui.theme.cardsElevation

@Preview
@Composable
fun TeamCardShimmer() {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = cardsElevation),
        shape = RoundedCornerShape(8.dp)
    ) {
        Shimmer { brush ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .padding(8.dp)
                            .height(24.dp)
                            .width(50.dp)
                            .background(brush = brush)
                    )

                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .height(100.dp)
                            .width(100.dp)
                            .background(brush = brush)
                    )

                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .height(100.dp)
                            .width(100.dp)
                            .background(brush = brush)
                    )
                }

                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .height(24.dp)
                        .fillMaxWidth()
                        .background(brush = brush)
                )

                Row (
                    modifier = Modifier.padding(16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .width(200.dp)
                            .height(48.dp)
                            .background(brush = brush)
                    )
                    Box(
                        modifier = Modifier
                            .padding(8.dp)
                            .width(144.dp)
                            .height(48.dp)
                            .background(brush = brush)
                    )
                }
            }
        }
    }
}