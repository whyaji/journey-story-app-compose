package com.whyaji.journey.ui.screen.addstory

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.whyaji.journey.JourneyApp
import com.whyaji.journey.viewmodel.StoryViewModel
import com.whyaji.journey.viewmodel.ViewModelFactory

fun getUriPermission(uri: Uri, context: Context) {
    val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION or
            Intent.FLAG_GRANT_WRITE_URI_PERMISSION
    context.contentResolver.takePersistableUriPermission(uri, takeFlags)
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddStory(navController: NavHostController) {
    val storyDao = JourneyApp.getInstance().getDb().StoryDao()
    val storyViewModel: StoryViewModel = viewModel(factory = ViewModelFactory(storyDao))

    val note = remember { mutableStateOf("") }
    val title = remember { mutableStateOf("") }
    val photo = remember { mutableStateOf("") }
    val saveState = remember { mutableStateOf(false) }

    val context = LocalContext.current
    val getImageRequest = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        if (uri != null) {
            getUriPermission(uri, context)
            photo.value = uri.toString()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Add Story",
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
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
            )
        },
        content = {
            Column(
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 80.dp,
                    bottom = 20.dp
                ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Image(
                        painter = rememberAsyncImagePainter(
                            ImageRequest
                                .Builder(context)
                                .data(data = Uri.parse(photo.value))
                                .build()
                        ),
                        contentDescription = "Selected Image",
                        modifier = Modifier
                            .size(250.dp)
                            .clip(shape = RoundedCornerShape(8.dp))
                            .border(
                                width = 1.dp,
                                color = Color.Gray,
                                shape = RoundedCornerShape(8.dp)
                            )
                    )
                    if (photo.value.isEmpty()) {
                        Text(
                            text = "Empty",
                            style = TextStyle(color = Color.Gray),
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { getImageRequest.launch(arrayOf("image/*")) },
                    modifier = Modifier.width(250.dp)
                ) {
                    Text(text = if (photo.value.isEmpty()) "Add Image" else "Change Image")
                }
                Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    value = title.value,
                    onValueChange = { title.value = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    TextField(
                        label = { Text("Note") },
                        value = note.value,
                        onValueChange = { note.value = it },
                        modifier = Modifier
                            .fillMaxSize()
                            .scrollable(
                                state = rememberScrollState(),
                                orientation = Orientation.Vertical
                            )
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        saveState.value = true
                        storyViewModel.addStory(
                            title.value,
                            note.value,
                            photo.value
                        )
                        navController.popBackStack()
                    },
                    enabled = title.value.isNotBlank() && note.value.isNotBlank(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Save")
                }
            }
        }
    )
}