package com.whyaji.journey.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.whyaji.journey.MainDestinations
import com.whyaji.journey.ui.screen.addstory.AddStory
import com.whyaji.journey.ui.screen.detail.Detail
import com.whyaji.journey.ui.screen.home.Home
import com.whyaji.journey.ui.screen.profile.Profile

fun NavGraphBuilder.homeNavGraph(
    navController: NavHostController
) {
    navigation(
        route = Graph.HOME,
        startDestination = Screen.Home.route
    ) {
        composable(route = Screen.Home.route) {
            Home(navController)
        }
        composable(route = Screen.AddStory.route) {
            AddStory(navController)
        }
        composable(route = Screen.Profile.route) {
            Profile(navController)
        }
        composable(
            route = "detail/{storyId}",
            arguments = listOf(navArgument("storyId") { type = NavType.IntType })
        ) { backStackEntry ->
            val arguments = requireNotNull(backStackEntry.arguments)
            val storyId = arguments.getInt("storyId")
            Detail(navController, storyId)
        }
    }
}