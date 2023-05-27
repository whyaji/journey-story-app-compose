package com.whyaji.journey

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.whyaji.journey.ui.navigation.RootNavigationGraph
import com.whyaji.journey.ui.theme.JourneyTheme
import com.whyaji.journey.util.DatastorePreference

class MainActivity : ComponentActivity() {

    private lateinit var dataStorePreference: DatastorePreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dataStorePreference = DatastorePreference.getInstance(this)

        setContent {
            JourneyTheme {
                val navController = rememberNavController()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RootNavigationGraph(navController = navController, dataStorePreference = dataStorePreference)
                }
            }
        }
    }
}