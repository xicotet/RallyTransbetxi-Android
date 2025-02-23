package com.canolabs.rallytransbetxi.ui.results

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.canolabs.rallytransbetxi.R
import androidx.compose.material3.Icon
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.canolabs.rallytransbetxi.data.models.responses.Result
import com.canolabs.rallytransbetxi.domain.entities.Language
import com.canolabs.rallytransbetxi.ui.teams.toOrdinal
import com.canolabs.rallytransbetxi.ui.theme.antaFamily
import com.canolabs.rallytransbetxi.ui.theme.cardsElevation
import com.canolabs.rallytransbetxi.ui.theme.ezraFamily

@Composable
fun ResultCard(
    result: Result,
    position: Int,
    onClick: () -> Unit,
    language: Language
) {
    Column {
        if (result.disqualified) {
            Text(
                text = stringResource(id = R.string.disqualified),
                fontFamily = antaFamily,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .padding(start = 16.dp, top = 8.dp)
            )
        }
        Card(
            modifier = Modifier
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = if (result.disqualified) 0.dp else 12.dp,
                    bottom = 12.dp
                )
                .fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = cardsElevation),
            shape = RoundedCornerShape(8.dp),
            onClick = onClick
        ) {

            val gradient = Brush.linearGradient(
                colors = listOf(
                    MaterialTheme.colorScheme.primaryContainer,
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)
                ),
                start = Offset(0f, 0f), // Start at the top left corner
                end = Offset(1000f, 1000f)
            )

            Row(
                modifier = Modifier
                    .background(brush = gradient)
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Box(
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        Text(
                            text = toOrdinal(position, language),
                            fontFamily = antaFamily,
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .align(Alignment.Center)
                        )

                        Text(
                            text = result.team.category.categoryId.substring(0, 5).uppercase(),
                            style = MaterialTheme.typography.bodyMedium,
                            fontFamily = antaFamily,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.5f),
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .align(Alignment.BottomCenter),
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .weight(0.8f)
                        .padding(start = 8.dp, top = 16.dp, end = 8.dp, bottom = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    // To peacefully accommodate german names which only consists of two words
                    val driverName = result.team.driver
                    val trimmedDriverName = if (driverName.split(" ").size > 2) {
                        driverName.substringBeforeLast(" ")
                    } else {
                        driverName
                    }

                    Text(
                        text = trimmedDriverName,
                        fontFamily = antaFamily,
                        textAlign = TextAlign.Center,
                        lineHeight = 24.sp,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 2,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // To peacefully accommodate german names which only consists of two words
                    val codriverName = result.team.codriver
                    val trimmedCodriverName = if (codriverName.split(" ").size > 2) {
                        codriverName.substringBeforeLast(" ")
                    } else {
                        codriverName
                    }

                    Text(
                        text = trimmedCodriverName,
                        fontFamily = antaFamily,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 24.sp,
                        textAlign = TextAlign.Center,
                        maxLines = 2,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painterResource(id = R.drawable.timer_outlined),
                            contentDescription = null
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = result.time,
                            fontFamily = antaFamily,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }

                    Box(
                        modifier = Modifier
                            .wrapContentWidth()
                            .border(
                                width = 2.dp,
                                color = MaterialTheme.colorScheme.onSurface,
                                shape = RoundedCornerShape(2.dp)
                            )
                            .background(
                                color = if (result.disqualified) MaterialTheme.colorScheme.error
                                else MaterialTheme.colorScheme.onPrimary,
                                shape = RoundedCornerShape(2.dp)
                            ),
                        contentAlignment = Alignment.Center,
                    ) {

                        Text(
                            text = "#" + result.number,
                            color = if (result.disqualified) MaterialTheme.colorScheme.onError
                            else MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.titleMedium,
                            fontFamily = ezraFamily,
                            modifier = Modifier.padding(start = 4.dp, end = 4.dp, top = 4.dp)
                        )
                    }
                }

                DriverImagesPager(result = result)
            }
        }
    }
}