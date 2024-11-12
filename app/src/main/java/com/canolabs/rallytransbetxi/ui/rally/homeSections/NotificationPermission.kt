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
fun NotificationPermission(viewModel: RallyScreenViewModel) {
    val activity = LocalContext.current as MainActivity
    val state = viewModel.state.collectAsState()
    var showBottomSheet by remember { mutableStateOf(false) }
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
                showBottomSheet = viewModel.shouldShowNotificationPermissionSheet()
                hasShownBottomSheet = showBottomSheet
            }
            delay(1000)
            viewModel.insertSettings()
        }
    }

    LaunchedEffect(showBottomSheet) {
        if (showBottomSheet) {
            coroutineScope.launch {
                permissionBottomSheetState.show()
            }
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            sheetState = permissionBottomSheetState,
            dragHandle = {},
            onDismissRequest = {
                coroutineScope.launch {
                    permissionBottomSheetState.hide()
                    showBottomSheet = false
                }
            },
        ) {
            NotificationPermissionBottomSheet(
                permissionLauncher = permissionLauncher,
                closeBottomSheet = {
                    coroutineScope.launch {
                        permissionBottomSheetState.hide()
                    }
                    showBottomSheet = false
                }
            )
        }
    }
}