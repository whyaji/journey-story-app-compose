package com.whyaji.journey.ui.screen.detail

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.whyaji.journey.JourneyApp
import com.whyaji.journey.util.Constants.storyDetailPlaceHolder
import com.whyaji.journey.viewmodel.StoryViewModel
import com.whyaji.journey.viewmodel.ViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Detail(navController: NavHostController, storyId: Int) {
    val scope = rememberCoroutineScope()
    val note = remember {
        mutableStateOf(storyDetailPlaceHolder)
    }
    val storyDao = JourneyApp.getInstance().getDb().StoryDao()
    val storyViewModel: StoryViewModel = viewModel(factory = ViewModelFactory(storyDao))

    LaunchedEffect(true) {
        scope.launch(Dispatchers.IO) {
            note.value = storyViewModel.getStory(storyId = storyId) ?: storyDetailPlaceHolder
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Detail",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        storyViewModel.deleteStory(note.value)
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete"
                        )
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        content = {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(vertical = 80.dp, horizontal = 16.dp)
            ) {
                if (note.value.imageUri != null && note.value.imageUri!!.isNotEmpty()) {
                    Image(
                        painter = rememberAsyncImagePainter(
                            ImageRequest.Builder(LocalContext.current)
                                .data(data = Uri.parse(note.value.imageUri))
                                .build()
                        ),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(6.dp),
                        contentScale = ContentScale.Crop
                    )
                }
                Text(
                    text = note.value.title,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(top = 24.dp, start = 12.dp, end = 24.dp),
                    fontWeight = FontWeight.Bold
                )
                Text(text = note.value.dateUpdated, Modifier.padding(12.dp))
                Text(text = note.value.note, Modifier.padding(12.dp))
            }
        }
    )
}