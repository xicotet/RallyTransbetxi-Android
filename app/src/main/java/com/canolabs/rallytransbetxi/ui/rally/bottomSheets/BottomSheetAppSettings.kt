package com.canolabs.rallytransbetxi.ui.rally.bottomSheets

import SettingsInfoDialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.canolabs.rallytransbetxi.BuildConfig
import com.canolabs.rallytransbetxi.ui.miscellaneous.Shimmer
import com.canolabs.rallytransbetxi.R
import com.canolabs.rallytransbetxi.domain.entities.DirectionsProfile
import com.canolabs.rallytransbetxi.domain.entities.Language
import com.canolabs.rallytransbetxi.domain.entities.Theme
import com.canolabs.rallytransbetxi.ui.rally.RallyScreenUIState
import com.canolabs.rallytransbetxi.ui.rally.RallyScreenViewModel

@Composable
fun BottomSheetAppSettings(
    state: RallyScreenUIState,
    viewModel: RallyScreenViewModel,
    darkThemeState: MutableState<Boolean>,
    fontScaleState: MutableState<Float>,
    changeLocale: (String) -> Unit
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.fetchThemeSettings()
        viewModel.fetchProfileSettings()
        viewModel.fetchFontSizeFactorSettings()
    }

    // Need to introduce these 2 since the fontScaleState seems not to affect text in this BottomSheet
    val scaledBodyMedium = MaterialTheme.typography.bodyMedium.copy(
        fontSize = MaterialTheme.typography.bodyMedium.fontSize * fontScaleState.value
    )
    val scaledTitleMedium = MaterialTheme.typography.titleMedium.copy(
        fontSize = MaterialTheme.typography.titleMedium.fontSize * fontScaleState.value
    )

    val showInfoDialog = remember { mutableStateOf(false) }

    if (state.language == null || state.theme == null || state.directionsProfile == null
        || state.fontSizeFactor == null
    ) {
        Shimmer {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(brush = it)
            )
        }
    } else if (showInfoDialog.value) {
        SettingsInfoDialog(showInfoDialog)
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
                        style = scaledBodyMedium
                    )

                    Text(
                        text = state.language.getLanguageName(),
                        style = scaledTitleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(
                        onClick = {
                            changeLocale.invoke(Language.SPANISH.getLanguageCode())
                            viewModel.setLanguage(Language.SPANISH)
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
                            changeLocale.invoke(Language.CATALAN.getLanguageCode())
                            viewModel.setLanguage(Language.CATALAN)
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
                            // TODO: Add onChangeLocale when German is added to the app
                            viewModel.setLanguage(Language.GERMAN)
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
                            // TODO: Add onChangeLocale when English is added to the app
                            viewModel.setLanguage(Language.ENGLISH)
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

            Spacer(modifier = Modifier.height(8.dp))

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
                        style = scaledBodyMedium
                    )
                    Text(
                        text = stringResource(id = state.theme.getName()),
                        style = scaledTitleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                val isSystemInDarkTheme = isSystemInDarkTheme()

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(
                        onClick = {
                            darkThemeState.value = isSystemInDarkTheme
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
                            darkThemeState.value = false
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
                            darkThemeState.value = true
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

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.Start,  // Aligns items to the start
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = stringResource(id = R.string.directions_mode),
                        style = scaledBodyMedium
                    )
                    Text(
                        text = stringResource(id = state.directionsProfile.getName()),
                        style = scaledTitleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                IconButton(
                    onClick = { showInfoDialog.value = true },
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        tint = MaterialTheme.colorScheme.primary,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IconButton(
                            onClick = {
                                viewModel.setProfile(DirectionsProfile.DRIVING_CAR)
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
                                    if (state.directionsProfile.getDatabaseName() == "driving-car")
                                        MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.onPrimary
                                )
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.directions_car),
                                contentDescription = null,
                                tint = if (state.directionsProfile.getDatabaseName() == "driving-car")
                                    MaterialTheme.colorScheme.onPrimary
                                else MaterialTheme.colorScheme.primary
                            )
                        }

                        IconButton(
                            onClick = {
                                viewModel.setProfile(DirectionsProfile.FOOT_WALKING)
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
                                    if (state.directionsProfile.getDatabaseName() == "foot-walking")
                                        MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.onPrimary
                                )
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.walk),
                                contentDescription = null,
                                tint = if (state.directionsProfile.getDatabaseName() == "foot-walking")
                                    MaterialTheme.colorScheme.onPrimary
                                else MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = stringResource(id = R.string.text_size),
                        style = scaledBodyMedium
                    )
                    Text(
                        text = stringResource(id = state.fontSizeFactor.name()),
                        style = scaledTitleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(
                        onClick = {
                            fontScaleState.value = state.fontSizeFactor.previous().value()
                            viewModel.setFontSizeFactor(state.fontSizeFactor.previous())
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
                            .background(MaterialTheme.colorScheme.onPrimary)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.remove),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(
                        onClick = {
                            fontScaleState.value = state.fontSizeFactor.next().value()
                            viewModel.setFontSizeFactor(state.fontSizeFactor.next())
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
                            .background(MaterialTheme.colorScheme.onPrimary)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = stringResource(id = R.string.app_version) + ": " + BuildConfig.VERSION_NAME,
                        style = scaledBodyMedium,
                        fontWeight = FontWeight.Light,
                    )
                    Text(
                        text = stringResource(id = R.string.developed_by_pablo_cano),
                        style = scaledBodyMedium,
                        fontWeight = FontWeight.Light,
                    )
                }
            }
        }
    }
}