package com.canolabs.rallytransbetxi.ui.rally.homeSections

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.canolabs.rallytransbetxi.ui.MainActivity
import com.canolabs.rallytransbetxi.ui.rally.RallyScreenViewModel
import com.canolabs.rallytransbetxi.ui.rally.bottomSheets.NotificationPermissionBottomSheet
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationPermission(
    viewModel: RallyScreenViewModel,
    showNotificationPermissionBottomSheet: MutableState<Boolean>
) {
    val activity = LocalContext.current as MainActivity
    val state = viewModel.state.collectAsState()
    var hasShownBottomSheet by remember { mutableStateOf(false) }
    val permissionBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            viewModel.setNotificationPermissionCounter(0)
            viewModel.insertSettings()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.fetchNotificationPermissionCounter()
    }

    // Monitor notification permission state
    LaunchedEffect(state.value.notificationPermissionCounter) {
        if (!hasShownBottomSheet && state.value.notificationPermissionCounter != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                viewModel.setNotificationPermissionCounter(0)
            } else {
                delay(500)
                showNotificationPermissionBottomSheet.value = viewModel.shouldShowNotificationPermissionSheet()
                hasShownBottomSheet = showNotificationPermissionBottomSheet.value
            }
            delay(1000)
            viewModel.insertSettings()
        }
    }

    LaunchedEffect(showNotificationPermissionBottomSheet) {
        if (showNotificationPermissionBottomSheet.value) {
            coroutineScope.launch {
                permissionBottomSheetState.show()
            }
        }
    }

    if (showNotificationPermissionBottomSheet.value) {
        ModalBottomSheet(
            sheetState = permissionBottomSheetState,
            dragHandle = {},
            onDismissRequest = {
                coroutineScope.launch {
                    permissionBottomSheetState.hide()
                    showNotificationPermissionBottomSheet.value = false
                }
            },
        ) {
            NotificationPermissionBottomSheet(
                permissionLauncher = permissionLauncher,
                closeBottomSheet = {
                    coroutineScope.launch {
                        permissionBottomSheetState.hide()
                    }
                    showNotificationPermissionBottomSheet.value = false
                }
            )
        }
    }
}