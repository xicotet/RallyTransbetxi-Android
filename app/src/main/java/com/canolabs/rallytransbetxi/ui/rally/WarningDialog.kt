package com.canolabs.rallytransbetxi.ui.rally

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.canolabs.rallytransbetxi.R
import com.canolabs.rallytransbetxi.data.models.responses.Warning
import com.canolabs.rallytransbetxi.domain.entities.Language
import com.canolabs.rallytransbetxi.ui.theme.robotoFamily

@Composable
fun WarningDialog(
    viewModel: RallyScreenViewModel,
) {
    val state = viewModel.state.collectAsState()

    Dialog(
        onDismissRequest = {
            viewModel.setIsDialogShowing(false)
        },
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = stringResource(id = R.string.warnings),
                        textAlign = TextAlign.Center,
                        fontFamily = robotoFamily,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = getWarningTitleByLanguage(
                        state.value.warningShownOnDialog!!,
                        state.value.language
                    ),
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .fillMaxWidth(),
                    fontFamily = robotoFamily,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                )

                Text(
                    text = getWarningContentByLanguage(
                        state.value.warningShownOnDialog!!,
                        state.value.language
                    ),
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .fillMaxWidth(),
                    fontFamily = robotoFamily,
                    textAlign = TextAlign.Start,
                )

                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .height(1.dp)
                )

                TextButton(
                    onClick = {
                        viewModel.setIsDialogShowing(false)
                    },
                    modifier = Modifier
                        .padding(bottom = 8.dp, end = 8.dp)
                        .align(Alignment.End),
                ) {
                    Text(
                        stringResource(id = R.string.ok),
                        fontFamily = robotoFamily
                    )
                }

            }
        }
    }
}

fun getWarningTitleByLanguage(warning: Warning, language: Language?): String {
    return when (language) {
        Language.SPANISH -> warning.titleEs
        Language.CATALAN -> warning.titleCa
        Language.ENGLISH -> warning.titleEn
        Language.GERMAN -> warning.titleDe
        null -> warning.titleEs
    }
}

fun getWarningContentByLanguage(warning: Warning, language: Language?): String {
    return when (language) {
        Language.SPANISH -> warning.contentEs
        Language.CATALAN -> warning.contentCa
        Language.ENGLISH -> warning.contentEn
        Language.GERMAN -> warning.contentDe
        null -> warning.contentEs
    }
}