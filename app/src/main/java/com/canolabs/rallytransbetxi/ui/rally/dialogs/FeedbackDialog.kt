package com.canolabs.rallytransbetxi.ui.rally.dialogs

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
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
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.canolabs.rallytransbetxi.R
import com.canolabs.rallytransbetxi.ui.theme.robotoFamily
import com.canolabs.rallytransbetxi.utils.Constants.Companion.CREATOR_EMAIL
import com.canolabs.rallytransbetxi.utils.Constants.Companion.SUBJECT_EMAIL


@Composable
fun FeedbackDialog(
    showFeedbackDialog: MutableState<Boolean>
) {
    val context = LocalContext.current

    Dialog(
        onDismissRequest = {
            showFeedbackDialog.value = false
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
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Icon(
                        painterResource(id = R.drawable.feedback),
                        contentDescription = null,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = stringResource(id = R.string.send_feedback),
                        textAlign = TextAlign.Center,
                        fontFamily = robotoFamily,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = stringResource(id = R.string.send_feedback_explanation),
                    modifier = Modifier
                        .padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 4.dp)
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

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    TextButton(
                        onClick = {
                            showFeedbackDialog.value = false
                        },
                        modifier = Modifier.padding(bottom = 8.dp, start = 8.dp),
                    ) {
                        Text(
                            stringResource(id = R.string.cancel),
                            fontFamily = robotoFamily
                        )
                    }

                    TextButton(
                        onClick = {
                            showFeedbackDialog.value = false

                            try {
                                // Open the default mail app with one destination email
                                val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                                    data = Uri.parse("mailto:") // Only email apps should handle this
                                    putExtra(Intent.EXTRA_EMAIL, arrayOf(CREATOR_EMAIL))
                                    putExtra(Intent.EXTRA_SUBJECT, SUBJECT_EMAIL)
                                }

                                context.startActivity(emailIntent)
                            } catch (e: Exception) {
                                Log.e("FeedbackDialog", "Failed to open email app", e)

                                Toast.makeText(
                                    context,
                                    context.getString(R.string.error_no_email_app),
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        },
                        modifier = Modifier.padding(bottom = 8.dp, end = 8.dp),
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
}