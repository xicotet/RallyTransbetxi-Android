package com.canolabs.rallytransbetxi.ui.rally.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.canolabs.rallytransbetxi.R
import com.canolabs.rallytransbetxi.data.models.responses.Statement
import com.canolabs.rallytransbetxi.domain.entities.Language
import com.canolabs.rallytransbetxi.ui.rally.RallyScreenViewModel
import com.canolabs.rallytransbetxi.ui.theme.robotoFamily

@Composable
fun StatementDialog(
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
                        painter = painterResource(id = R.drawable.mail),
                        contentDescription = null,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = stringResource(id = R.string.statements),
                        textAlign = TextAlign.Center,
                        fontFamily = robotoFamily,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = getStatementTitleByLanguage(
                        state.value.statementShownOnDialog!!,
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
                    text = getStatementContentByLanguage(
                        state.value.statementShownOnDialog!!,
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
                        .height(1.dp),
                    color = MaterialTheme.colorScheme.primary
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

fun getStatementTitleByLanguage(statement: Statement, language: Language?): String {
    return when (language) {
        Language.SPANISH -> statement.titleEs
        Language.CATALAN -> statement.titleCa
        Language.ENGLISH -> statement.titleEn
        Language.GERMAN -> statement.titleDe
        null -> statement.titleEs
    }
}

fun getStatementContentByLanguage(statement: Statement, language: Language?): String {
    return when (language) {
        Language.SPANISH -> statement.contentEs
        Language.CATALAN -> statement.contentCa
        Language.ENGLISH -> statement.contentEn
        Language.GERMAN -> statement.contentDe
        null -> statement.contentEs
    }
}