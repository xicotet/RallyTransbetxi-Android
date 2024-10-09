package com.canolabs.rallytransbetxi.ui.teams

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.canolabs.rallytransbetxi.R
import com.canolabs.rallytransbetxi.ui.theme.PaddingHuge
import com.canolabs.rallytransbetxi.ui.theme.PaddingLarge
import com.canolabs.rallytransbetxi.ui.theme.PaddingMedium
import com.canolabs.rallytransbetxi.ui.theme.PaddingRegular
import com.canolabs.rallytransbetxi.ui.theme.ezraFamily

@Composable
fun TeamsScreenHeader(
    viewModel: TeamsScreenViewModel,
) {
    val state by viewModel.state.collectAsState()

    if (state.isSearchBarVisible) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { viewModel.setIsSearchBarVisible(false) },
                modifier = Modifier
                    .padding(top = PaddingLarge, bottom = PaddingLarge, start = PaddingMedium)
                    .size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }

            TextField(
                value = state.searchText,
                onValueChange = viewModel::setSearchText,
                modifier = Modifier
                    .padding(vertical = PaddingLarge, horizontal = PaddingMedium)
                    .height(56.dp)
                    .fillMaxWidth()
                    .border(
                        2.dp,
                        MaterialTheme.colorScheme.onSurface,
                        MaterialTheme.shapes.extraLarge
                    ),
                colors = TextFieldDefaults.colors().copy(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = MaterialTheme.colorScheme.onSurface
                ),
                shape = MaterialTheme.shapes.small,
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
                    IconButton(
                        onClick = { viewModel.setSearchText("") },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.close),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            )
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