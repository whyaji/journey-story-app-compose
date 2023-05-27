package com.whyaji.journey.ui.navigation

import com.whyaji.journey.MainDestinations

sealed class Screen(val route: String){
    object Walkthrough: Screen(MainDestinations.WALKTHROUGH_ROUTE)
    object Home: Screen(MainDestinations.HOME_ROUTE)
    object Profile: Screen(MainDestinations.PROFILE_ROUTE)
    object AddStory: Screen(MainDestinations.ADD_STORY_ROUTE)
    object Detail: Screen("detail/{storyId}") {
        fun createRoute(storyId: Int) = "detail/$storyId"
    }
}