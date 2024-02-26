package com.canolabs.rallytransbetxi.ui.results

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.canolabs.rallytransbetxi.data.models.responses.Result
import com.canolabs.rallytransbetxi.ui.theme.robotoFamily

@Composable
fun ResultCard(result: Result) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(8.dp),
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = result.team.name,
                style = MaterialTheme.typography.bodyMedium,
                fontFamily = robotoFamily
            )
            Text(
                text = "Score: ${result.team.driver}",
                style = MaterialTheme.typography.bodySmall,
                fontFamily = robotoFamily
            )
            Text(
                text = "Position: ${result.team.codriver}",
                style = MaterialTheme.typography.bodySmall,
                fontFamily = robotoFamily
            )
        }
    }
}