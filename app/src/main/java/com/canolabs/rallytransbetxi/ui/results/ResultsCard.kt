package com.canolabs.rallytransbetxi.ui.results

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.canolabs.rallytransbetxi.R
import androidx.compose.material3.Icon
import androidx.compose.material3.VerticalDivider
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import coil.compose.rememberAsyncImagePainter
import com.canolabs.rallytransbetxi.data.models.responses.Result
import com.canolabs.rallytransbetxi.ui.theme.ezraFamily
import com.canolabs.rallytransbetxi.ui.theme.robotoFamily

@Composable
fun ResultCard(result: Result) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .height(150.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        shape = RoundedCornerShape(8.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Place number 1
            Text(
                text = "1",
                fontFamily = ezraFamily,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .weight(0.15f)
                    .padding(start = 8.dp)
            )

            VerticalDivider(thickness = 4.dp, color = MaterialTheme.colorScheme.primary)

            // Column with three Texts
            Column(
                modifier = Modifier
                    .weight(0.8f)
                    .padding(start = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = result.team.driver.substringBeforeLast(" "),
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = result.team.codriver.substringBeforeLast(" "),
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painterResource(id = R.drawable.timelapse_outlined),
                        contentDescription = null
                    )
                    Text(
                        text = result.time,
                        fontFamily = robotoFamily,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
            Log.d("ResultCard", "Image URL: ${result.team.driverImage}")
            // Image on the right side
            Image( // The Image component to load the image with the Coil library
                painter = rememberAsyncImagePainter(model = result.team.driverImage),
                contentDescription = null,
                modifier = Modifier.clip(CircleShape).height(48.dp).width(48.dp)
            )
        }
    }
}