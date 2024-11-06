package com.canolabs.rallytransbetxi.ui.rally.bottomSheets

import android.Manifest
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.canolabs.rallytransbetxi.R
import com.canolabs.rallytransbetxi.ui.theme.PaddingMedium
import com.canolabs.rallytransbetxi.ui.theme.ezraFamily
import com.canolabs.rallytransbetxi.ui.theme.robotoFamily

@Composable
fun NotificationPermissionBottomSheet(
    closeBottomSheet: () -> Unit,
    permissionLauncher: ManagedActivityResultLauncher<String, Boolean>,
) {
    Column(
        modifier = Modifier.padding(PaddingMedium),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.padding(8.dp))

        Image(
            painter = painterResource(id = R.drawable.door_bell),
            modifier = Modifier.size(96.dp),
            contentDescription = null
        )

        Spacer(modifier = Modifier.padding(8.dp))

        Text(
            text = stringResource(id = R.string.permission_notifications_title),
            fontFamily = ezraFamily,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(PaddingMedium),
        )

        Text(
            text = stringResource(id = R.string.permission_notifications_message),
            fontFamily = robotoFamily,
            modifier = Modifier.padding(PaddingMedium),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.padding(8.dp))

        ElevatedButton(
            onClick = {
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                closeBottomSheet()
            },
            modifier = Modifier.height(48.dp).fillMaxWidth().padding(horizontal = 16.dp),
            colors = ButtonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                disabledContainerColor = MaterialTheme.colorScheme.primary,
                disabledContentColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(text = stringResource(id = R.string.allow_notifications))
        }

        Spacer(modifier = Modifier.padding(8.dp))

        ElevatedButton(
            onClick = closeBottomSheet,
            modifier = Modifier.height(48.dp).fillMaxWidth().padding(horizontal = 16.dp),
            colors = ButtonColors(
                containerColor = MaterialTheme.colorScheme.onPrimary,
                contentColor = MaterialTheme.colorScheme.primary,
                disabledContainerColor = MaterialTheme.colorScheme.primary,
                disabledContentColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(text = stringResource(id = R.string.omit))
        }

        Spacer(modifier = Modifier.padding(8.dp))
    }
}