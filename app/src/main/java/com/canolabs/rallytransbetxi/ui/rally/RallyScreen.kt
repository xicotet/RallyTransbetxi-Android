package com.canolabs.rallytransbetxi.ui.rally

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.canolabs.rallytransbetxi.R
import com.canolabs.rallytransbetxi.ui.theme.antaFamily
import com.canolabs.rallytransbetxi.ui.theme.ruralColor
import com.canolabs.rallytransbetxi.utils.DateTimeUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun RallyScreen() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.transbetxi_header),
            contentDescription = null
        )
        CountdownTimer()
    }
}

@Composable
fun CountdownTimer() {
    var timeLeft by remember { mutableLongStateOf(DateTimeUtils.secondUntilStartOfEvent()) }
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .border(2.dp, ruralColor, MaterialTheme.shapes.extraLarge),
        contentAlignment = Alignment.Center
    ) {
        val days = timeLeft / (60 * 60 * 24)
        val hours = (timeLeft / (60 * 60)) % 24
        val minutes = (timeLeft / 60) % 60
        val seconds = timeLeft % 60

        AnimatedVisibility(visible = timeLeft > 0) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = String.format("%02dd %02dh %02dm %02ds", days, hours, minutes, seconds),
                    fontFamily = antaFamily,
                    fontSize = 30.sp,
                    color = if (isSystemInDarkTheme()) Color.White else ruralColor,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Image(
                    painter = painterResource(id = R.drawable.ruralnostra),
                    contentDescription = null
                )
            }

        }

        LaunchedEffect(key1 = Unit) {
            coroutineScope.launch {
                while (timeLeft > 0) {
                    delay(1000L) // delay for 1 second
                    timeLeft--
                }
            }
        }
    }

    if (timeLeft <= 0) {
        Image(
            painter = painterResource(id = R.drawable.ruralnostra),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            contentDescription = null
        )
    }
}