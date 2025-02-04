package com.canolabs.rallytransbetxi.ui.navigation

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.ui.unit.IntOffset

class NavigationTransitions {

    companion object {
        val enterTransition =
            slideIn(tween(400, easing = LinearEasing)) { fullSize ->
                IntOffset(fullSize.width, 0)
            }

        val exitTransition = slideOut(tween(400, easing = LinearEasing)) { fullSize ->
            IntOffset(-(fullSize.width), 0)
        }

        val popEnterTransition = slideIn(tween(400, easing = LinearEasing)) { fullSize ->
            IntOffset(-(fullSize.width), 0)
        }

        val popExitTransition = slideOut(tween(400, easing = LinearEasing)) { fullSize ->
            IntOffset(fullSize.width, 0)
        }

        val enterFromBottomTransition =
            slideIn(tween(400, easing = LinearEasing)) { fullSize ->
                IntOffset(0, fullSize.height)
            }

        val exitToBottomTransition = slideOut(tween(400, easing = LinearEasing)) { fullSize ->
            IntOffset(0, -fullSize.height)
        }

        val popEnterFromBottomTransition = slideIn(tween(400, easing = LinearEasing)) { fullSize ->
            IntOffset(0, -fullSize.height)
        }

        val popExitToBottomTransition = slideOut(tween(400, easing = LinearEasing)) { fullSize ->
            IntOffset(0, fullSize.height)
        }
    }
}