package com.canolabs.rallytransbetxi.ui.results

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.material3.VerticalDivider
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.canolabs.rallytransbetxi.data.models.responses.Result
import com.canolabs.rallytransbetxi.ui.theme.ezraFamily
import com.canolabs.rallytransbetxi.ui.theme.robotoFamily

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ResultCard(result: Result) {

    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        val pagerState = rememberPagerState(pageCount = { 2 })

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Place number 1
            Text(
                text = "1",
                fontFamily = ezraFamily,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .weight(0.15f)
                    .padding(start = 8.dp)
            )

            VerticalDivider(thickness = 4.dp, color = MaterialTheme.colorScheme.primary)

            // Column with three Texts
            Column(
                modifier = Modifier
                    .weight(0.8f)
                    .padding(start = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = result.team.driver.substringBeforeLast(" "),
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = result.team.codriver.substringBeforeLast(" "),
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painterResource(id = R.drawable.timelapse_outlined),
                        contentDescription = null
                    )
                    Text(
                        text = result.time,
                        fontFamily = robotoFamily,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
            Log.d("ResultCard", "Image URL: ${result.team.driverImage}")

            val driverPainter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(result.team.driverImage)
                    .size(coil.size.Size.ORIGINAL)
                    .build(),
            )

            val codriverPainter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(result.team.codriverImage)
                    .size(coil.size.Size.ORIGINAL)
                    .build(),
            )

            Column (
                modifier = Modifier
                    .padding(end = 8.dp),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center

            ) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .height(100.dp)
                        .width(100.dp)
                        .padding(start = 16.dp)
                ) {page ->
                    when (page) {
                        0 -> {
                            if (driverPainter.state is AsyncImagePainter.State.Loading) {
                                Shimmer { brush ->
                                    Box(
                                        modifier = Modifier
                                            .clip(CircleShape)
                                            .height(96.dp)
                                            .width(96.dp)
                                            .background(brush = brush)
                                    )
                                }
                            } else {
                                Image(
                                    painter = driverPainter,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .height(96.dp)
                                        .width(96.dp)
                                )
                            }
                        }

                        1 -> {
                            if (codriverPainter.state is AsyncImagePainter.State.Loading) {
                                Shimmer { brush ->
                                    Box(
                                        modifier = Modifier
                                            .clip(CircleShape)
                                            .height(96.dp)
                                            .width(96.dp)
                                            .background(brush = brush)
                                    )
                                }
                            } else {
                                Image(
                                    painter = codriverPainter,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .height(96.dp)
                                        .width(96.dp)
                                )
                            }
                        }
                    }
                }
                Row(
                    Modifier
                        .wrapContentHeight()
                        .align(Alignment.CenterHorizontally)
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    repeat(pagerState.pageCount) { iteration ->
                        val color =
                            if (pagerState.currentPage == iteration) MaterialTheme.colorScheme.onPrimaryContainer
                            else Color.LightGray
                        Box(
                            modifier = Modifier
                                .padding(2.dp)
                                .clip(CircleShape)
                                .background(color)
                                .size(16.dp)
                        )
                    }
                }
            }
        }
    }
}