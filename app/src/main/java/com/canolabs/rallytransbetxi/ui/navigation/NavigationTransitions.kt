package com.canolabs.rallytransbetxi.ui.navigation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.ui.unit.IntOffset

class NavigationTransitions {

    companion object {
        val enterTransition =
            slideIn(tween(300, easing = FastOutSlowInEasing)) { fullSize ->
                IntOffset(fullSize.width, 0)
            }

        val exitTransition = slideOut(tween(300, easing = FastOutSlowInEasing)) { fullSize ->
            IntOffset(-(fullSize.width), 0)
        }

        val popEnterTransition = slideIn(tween(300, easing = FastOutSlowInEasing)) { fullSize ->
            IntOffset(-(fullSize.width), 0)
        }

        val popExitTransition = slideOut(tween(300, easing = FastOutSlowInEasing)) { fullSize ->
            IntOffset(fullSize.width, 0)
        }

        val enterFromBottomTransition =
            slideIn(tween(300, easing = FastOutSlowInEasing)) { fullSize ->
                IntOffset(0, fullSize.height)
            }

        val exitToBottomTransition = slideOut(tween(300, easing = FastOutSlowInEasing)) { fullSize ->
            IntOffset(0, -fullSize.height)
        }

        val popEnterFromBottomTransition = slideIn(tween(300, easing = FastOutSlowInEasing)) { fullSize ->
            IntOffset(0, -fullSize.height)
        }

        val popExitToBottomTransition = slideOut(tween(300, easing = FastOutSlowInEasing)) { fullSize ->
            IntOffset(0, fullSize.height)
        }
    }
}