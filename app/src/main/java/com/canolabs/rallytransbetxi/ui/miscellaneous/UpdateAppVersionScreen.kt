package com.canolabs.rallytransbetxi.ui.miscellaneous

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.canolabs.rallytransbetxi.R
import com.canolabs.rallytransbetxi.ui.theme.ezraFamily
import com.canolabs.rallytransbetxi.ui.theme.onboardingButtonDisabledBackground
import com.canolabs.rallytransbetxi.ui.theme.onboardingButtonDisabledContent
import com.canolabs.rallytransbetxi.ui.theme.robotoFamily
import com.canolabs.rallytransbetxi.ui.theme.seed

@Composable
fun UpdateAppVersionScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = seed),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Image(
            imageVector = Icons.Default.Refresh,
            contentDescription = null,
            modifier = Modifier
                .size(120.dp)
        )

        Spacer(modifier = Modifier.padding(8.dp))

        Text(
            text = stringResource(id = R.string.new_app_version),
            fontFamily = ezraFamily,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp),
        )

        Text(
            text = stringResource(id = R.string.new_app_version_explained),
            fontFamily = robotoFamily,
            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        ElevatedButton(
            onClick = {
                // TODO: Redirect to the Play Store
            },
            colors = ButtonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                disabledContainerColor = onboardingButtonDisabledBackground,
                disabledContentColor = onboardingButtonDisabledContent
            ),
            shape = RoundedCornerShape(32),
            modifier = Modifier
                .fillMaxWidth()
                .height(96.dp)
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.update_now),
                style = MaterialTheme.typography.bodyMedium,
                fontFamily = robotoFamily,
                fontWeight = FontWeight.Bold
            )
        }
    }
}