package com.canolabs.rallytransbetxi.ui.rally

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.canolabs.rallytransbetxi.R
import com.canolabs.rallytransbetxi.ui.theme.ezraFamily

@Composable
fun FeaturedSection(
    viewModel: RallyScreenViewModel
) {
    LazyRow {
        item {
            Column(
                modifier = Modifier
                    .width(140.dp)
                    .padding(top = 16.dp, start = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(
                    onClick = {
                       viewModel.setIsSettingsBottomSheetVisible(true)
                    },
                    modifier = Modifier
                        .size(120.dp)
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.onSurface,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable {
                            // This clickable section needs to be added in order
                            // to make the whole button clickable
                            viewModel.setIsSettingsBottomSheetVisible(true)
                        }
                        .background(brush = getRallyScreenCardsGradient())
                        .align(Alignment.CenterHorizontally)
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.onSurface,
                        contentDescription = null
                    )
                }

                Text(
                    text = stringResource(id = R.string.settings),
                    style = MaterialTheme.typography.bodyLarge,
                    fontFamily = ezraFamily,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(top = 8.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
        item {
            Column(
                modifier = Modifier
                    .width(140.dp)
                    .padding(top = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .size(120.dp)
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.onSurface,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .background(brush = getRallyScreenCardsGradient())
                        .align(Alignment.CenterHorizontally)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.trophy_outlined),
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.onSurface,
                        contentDescription = null
                    )
                }

                Text(
                    text = stringResource(id = R.string.list_of_champions),
                    style = MaterialTheme.typography.bodyLarge,
                    fontFamily = ezraFamily,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(top = 8.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
        item {
            Column(
                modifier = Modifier
                    .width(140.dp)
                    .padding(top = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .size(120.dp)
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.onSurface,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .background(brush = getRallyScreenCardsGradient())
                        .align(Alignment.CenterHorizontally)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.sponsors),
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.onSurface,
                        contentDescription = null
                    )
                }

                Text(
                    text = stringResource(id = R.string.sponsors),
                    style = MaterialTheme.typography.bodyLarge,
                    fontFamily = ezraFamily,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(top = 8.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
        item {
            Column(
                modifier = Modifier
                    .width(140.dp)
                    .padding(top = 16.dp, end = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .size(120.dp)
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.onSurface,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .background(brush = getRallyScreenCardsGradient())
                        .align(Alignment.CenterHorizontally)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.restaurant_outlined),
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.onSurface,
                        contentDescription = null
                    )
                }

                Text(
                    text = stringResource(id = R.string.where_to_eat),
                    style = MaterialTheme.typography.bodyLarge,
                    fontFamily = ezraFamily,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(top = 8.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}