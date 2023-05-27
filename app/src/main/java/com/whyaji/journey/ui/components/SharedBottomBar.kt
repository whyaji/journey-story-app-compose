package com.whyaji.journey.ui.components

import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Create
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.whyaji.journey.ui.navigation.BottomNavItem
import com.whyaji.journey.ui.navigation.Screen

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "CheckResult")
@Composable
fun SharedBottomBar(
    navController: NavController,
    containerColor: Color = MaterialTheme.colorScheme.primary
) {
    val bottomNavItems = listOf(
        BottomNavItem(
            name = "Home",
            route = Screen.Home.route,
            icon = Icons.Rounded.Home,
        ),
        BottomNavItem(
            name = "Create",
            route = Screen.AddStory.route,
            icon = Icons.Rounded.Create,
        ),
        BottomNavItem(
            name = "Profile",
            route = Screen.Profile.route,
            icon = Icons.Rounded.Person,
        ),
    )

    val backStackEntry = navController.currentBackStackEntryAsState()
    NavigationBar(containerColor = containerColor) {
        bottomNavItems.forEach { item ->
            val selected = item.route == backStackEntry.value?.destination?.route

            NavigationBarItem(
                selected = selected,
                onClick = {
                    if (item.name != "Create") navController.popBackStack()
                    navController.navigate(item.route)
                },
                label = {
                    Text(
                        text = item.name,
                        fontWeight = FontWeight.SemiBold,
                    )
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = "${item.name} Icon",
                    )
                }
            )
        }
    }
}