package com.canolabs.rallytransbetxi.ui.teams

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.canolabs.rallytransbetxi.R
import com.canolabs.rallytransbetxi.ui.theme.PaddingHuge
import com.canolabs.rallytransbetxi.ui.theme.PaddingLarge
import com.canolabs.rallytransbetxi.ui.theme.PaddingMedium
import com.canolabs.rallytransbetxi.ui.theme.PaddingRegular
import com.canolabs.rallytransbetxi.ui.theme.PaddingSmall
import com.canolabs.rallytransbetxi.ui.theme.ezraFamily

@Composable
fun TeamsScreenHeader(
    viewModel: TeamsScreenViewModel,
) {
    val state by viewModel.state.collectAsState()

    if (state.isSearchBarVisible) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = PaddingLarge, horizontal = PaddingMedium),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = state.searchText,
                onValueChange = viewModel::setSearchText,
                modifier = Modifier
                    .padding(end = PaddingSmall)
                    .height(56.dp)
                    .clip(RoundedCornerShape(32))
                    .border(
                        2.dp,
                        MaterialTheme.colorScheme.onSurface,
                        RoundedCornerShape(32)
                    ),
                colors = TextFieldDefaults.colors().copy(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = MaterialTheme.colorScheme.onSurface
                ),
                shape = RoundedCornerShape(32),
                singleLine = true,
                placeholder = { Text(stringResource(id = R.string.search)) },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.search),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                },
                trailingIcon = {
                    AnimatedVisibility(visible = state.searchText.isNotEmpty()) {
                        IconButton(
                            onClick = { viewModel.setSearchText("") },
                            modifier = Modifier.size(24.dp),
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.close),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            )

            IconButton(
                onClick = { viewModel.setIsSearchBarVisible(false) },
                modifier = Modifier
                    .size(24.dp)
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.onSurface,
                        shape = RoundedCornerShape(32)
                    ),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.close),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    } else {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(id = R.string.teams),
                style = MaterialTheme.typography.displaySmall,
                fontFamily = ezraFamily,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(
                        top = PaddingHuge,
                        start = PaddingRegular,
                        end = PaddingHuge,
                        bottom = PaddingLarge
                    )
            )
            IconButton(
                onClick = { viewModel.setIsSearchBarVisible(true) },
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(horizontal = PaddingRegular)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.search),
                    contentDescription = null,
                    modifier = Modifier.size(48.dp)
                )
            }
        }
    }
}