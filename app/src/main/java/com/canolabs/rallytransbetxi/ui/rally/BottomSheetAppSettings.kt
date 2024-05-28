package com.canolabs.rallytransbetxi.ui.rally

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.canolabs.rallytransbetxi.ui.miscellaneous.Shimmer
import com.canolabs.rallytransbetxi.R
import com.canolabs.rallytransbetxi.domain.entities.Language
import com.canolabs.rallytransbetxi.domain.entities.Theme

@Composable
fun BottomSheetAppSettings(
    state: RallyScreenUIState,
    viewModel: RallyScreenViewModel
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.fetchLanguageSettings()
        viewModel.fetchThemeSettings()
        viewModel.fetchProfileSettings()
    }

    if (state.language == null || state.theme == null || state.directionsProfile == null) {
        Shimmer {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(brush = it)
            )
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.surface)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = stringResource(id = R.string.language),
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Text(
                        text = state.language.getLanguageName(),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(
                        onClick = {
                            viewModel.setLanguage(Language.SPANISH)
                            viewModel.insertSettings()
                        },
                        modifier = Modifier
                            .size(48.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.flag_of_spain),
                            contentDescription = null,
                            modifier = Modifier
                                .clip(RectangleShape)
                                .let { modifier ->
                                    if (state.language.getDatabaseName() == "spanish") {
                                        modifier.border(
                                            2.dp,
                                            MaterialTheme.colorScheme.primary,
                                            CircleShape
                                        )
                                    } else {
                                        modifier
                                    }
                                }
                        )
                    }
                    IconButton(
                        onClick = {
                            viewModel.setLanguage(Language.CATALAN)
                            viewModel.insertSettings()
                        },
                        modifier = Modifier
                            .size(60.dp, 48.dp)
                            .clip(RectangleShape)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.flag_of_the_land_of_valencia),
                            contentDescription = null,
                            modifier = Modifier
                                .clip(RectangleShape)
                                .let { modifier ->
                                    if (state.language.getDatabaseName() == "catalan") {
                                        modifier.border(
                                            2.dp,
                                            MaterialTheme.colorScheme.primary,
                                            RectangleShape
                                        )
                                    } else {
                                        modifier
                                    }
                                }
                        )
                    }
                    IconButton(
                        onClick = {
                            viewModel.setLanguage(Language.GERMAN)
                            viewModel.insertSettings()
                        },
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RectangleShape)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.flag_of_germany),
                            contentDescription = null,
                            modifier = Modifier
                                .clip(RectangleShape)
                                .let { modifier ->
                                    if (state.language.getDatabaseName() == "german") {
                                        modifier.border(
                                            2.dp,
                                            MaterialTheme.colorScheme.primary,
                                            CircleShape
                                        )
                                    } else {
                                        modifier
                                    }
                                }
                        )
                    }
                    IconButton(
                        onClick = {
                            viewModel.setLanguage(Language.ENGLISH)
                            viewModel.insertSettings()
                        },
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RectangleShape)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.flag_of_united_kingdom),
                            contentDescription = null,
                            modifier = Modifier
                                .clip(RectangleShape)
                                .let { modifier ->
                                    if (state.language.getDatabaseName() == "english") {
                                        modifier.border(
                                            2.dp,
                                            MaterialTheme.colorScheme.primary,
                                            CircleShape
                                        )
                                    } else {
                                        modifier
                                    }
                                }

                        )
                    }
                }

            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = stringResource(id = R.string.theme),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = stringResource(id = state.theme.getName()),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(
                        onClick = {
                            viewModel.setTheme(Theme.AUTO)
                            viewModel.insertSettings()
                        },
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.primary,
                                CircleShape
                            )
                            .background(
                                if (state.theme.getDatabaseName() == "auto")
                                    MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onPrimary
                            )
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.auto),
                            contentDescription = null,
                            tint = if (state.theme.getDatabaseName() == "auto")
                                MaterialTheme.colorScheme.onPrimary
                            else MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(
                        onClick = {
                            viewModel.setTheme(Theme.LIGHT)
                            viewModel.insertSettings()
                        },
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.primary,
                                CircleShape
                            )
                            .background(
                                if (state.theme.getDatabaseName() == "light")
                                    MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onPrimary
                            )
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.light_mode),
                            contentDescription = null,
                            tint = if (state.theme.getDatabaseName() == "light")
                                MaterialTheme.colorScheme.onPrimary
                            else MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(
                        onClick = {
                            viewModel.setTheme(Theme.DARK)
                            viewModel.insertSettings()
                        },
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.primary,
                                CircleShape
                            )
                            .background(
                                if (state.theme.getDatabaseName() == "dark")
                                    MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onPrimary
                            )
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.dark_mode),
                            contentDescription = null,
                            tint = if (state.theme.getDatabaseName() == "dark")
                                MaterialTheme.colorScheme.onPrimary
                            else MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}