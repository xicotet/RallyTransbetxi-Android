package com.canolabs.rallytransbetxi.ui.stages

import android.icu.util.Calendar
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.canolabs.rallytransbetxi.data.models.responses.Stage
import com.canolabs.rallytransbetxi.R
import com.canolabs.rallytransbetxi.ui.theme.PaddingMedium
import com.canolabs.rallytransbetxi.ui.theme.PaddingRegular
import com.canolabs.rallytransbetxi.ui.theme.PaddingSmall
import com.canolabs.rallytransbetxi.ui.theme.ezraFamily
import com.canolabs.rallytransbetxi.ui.theme.robotoFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StageCard(stage: Stage) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(PaddingSmall),
        onClick = { /* Handle click event */ },
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stage.acronym,
                    style = MaterialTheme.typography.headlineMedium,
                    fontFamily = ezraFamily,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.padding(PaddingMedium)
                )
                Text(
                    text = stage.name,
                    fontFamily = robotoFamily,
                    style = MaterialTheme.typography.titleLarge
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.route_filled),
                    contentDescription = "Route icon",
                    modifier = Modifier
                        .padding(PaddingMedium)
                        .size(36.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${stage.distance} km",
                    fontFamily = robotoFamily,
                    style = MaterialTheme.typography.titleMedium,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Icon(
                    painter = painterResource(id = R.drawable.schedule_filled),
                    contentDescription = "Clock icon",
                    modifier = Modifier
                        .padding(
                            start = PaddingRegular,
                            end = PaddingSmall
                        )
                        .size(24.dp),
                )
                Spacer(modifier = Modifier.width(8.dp))
                val date = stage.startTime?.toDate()
                val calendar = Calendar.getInstance().apply {
                    time = date
                }
                val hour = calendar.get(Calendar.HOUR_OF_DAY)
                val minute = String.format("%02d", calendar.get(Calendar.MINUTE))
                Text(
                    text = "$hour:$minute",
                    fontFamily = robotoFamily,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}