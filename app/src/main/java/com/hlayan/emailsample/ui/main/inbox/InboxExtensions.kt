package com.hlayan.emailsample.ui.main.inbox

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.Navigator

/**
 * The following two extension function is needed to navigate safely
 *
 * Eg.
 * When you click two list item at the same time, then
 * you get app cracking. Here are reference link for you
 *
 * https://github.com/android/sunflower/pull/551/commits/2a0ac2ce74581692d325a9644552eb429b2298ac#
 *
 */
fun NavController.navigateSafe(
    navDirections: NavDirections,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null
) {
    navigateSafe(navDirections.actionId, navDirections.arguments, navOptions, navigatorExtras)
}

fun NavController.navigateSafe(
    @IdRes resId: Int,
    args: Bundle? = null,
    navOptions: NavOptions? = null,
    navExtras: Navigator.Extras? = null
) {
    val action = currentDestination?.getAction(resId) ?: graph.getAction(resId)
    if (action != null && currentDestination?.id != action.destinationId) {
        navigate(resId, args, navOptions, navExtras)
    }
}