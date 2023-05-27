package com.whyaji.journey.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.whyaji.journey.util.DatastorePreference

@Composable
fun RootNavigationGraph(
    navController: NavHostController,
    dataStorePreference: DatastorePreference
) {
    val token = remember { mutableStateOf("") }
    val isLoading = remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        val fetchedToken = dataStorePreference.getToken()
        if (!fetchedToken.isNullOrEmpty()) {
            token.value = fetchedToken
        }
        isLoading.value = false
    }

    if (isLoading.value) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        val startDestination = if (token.value.isEmpty()) Graph.AUTHENTICATION else Graph.HOME
        NavHost(
            navController = navController,
            route = Graph.ROOT,
            startDestination = startDestination
        ) {
            authNavGraph(navController = navController, dataStorePreference)
            homeNavGraph(navController = navController)
        }
    }
}

object Graph {
    const val ROOT = "root_graph"
    const val AUTHENTICATION = "auth_graph"
    const val HOME = "home_graph"
}