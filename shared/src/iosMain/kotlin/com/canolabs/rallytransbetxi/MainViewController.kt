package com.canolabs.rallytransbetxi

import androidx.compose.ui.window.ComposeUIViewController
import com.canolabs.rallytransbetxi.data.getDatabaseBuilder
import com.canolabs.rallytransbetxi.shared.App
import com.canolabs.rallytransbetxi.shared.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) {
    App()
    // TODO()
}