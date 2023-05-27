package com.whyaji.journey.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.whyaji.journey.ui.screen.walkthrough.Walkthrough
import com.whyaji.journey.util.DatastorePreference

fun NavGraphBuilder.authNavGraph(
    navController: NavHostController,
    dataStorePreference: DatastorePreference
) {
    navigation(
        route = Graph.AUTHENTICATION,
        startDestination = Screen.Walkthrough.route
    ) {
        composable(route = Screen.Walkthrough.route) {
            Walkthrough(navController, dataStorePreference)
        }
    }
}