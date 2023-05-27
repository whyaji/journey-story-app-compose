package com.whyaji.journey.ui.screen.liststory

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.whyaji.journey.JourneyApp
import com.whyaji.journey.model.Story
import com.whyaji.journey.ui.navigation.Screen
import com.whyaji.journey.util.Constants.orPlaceHolderList
import com.whyaji.journey.util.getDayFormat
import com.whyaji.journey.viewmodel.StoryViewModel
import com.whyaji.journey.viewmodel.ViewModelFactory

@SuppressLint("RememberReturnType")
@Composable
fun ListStory(navController: NavController) {
    val storyDao = JourneyApp.getInstance().getDb().StoryDao()
    val storyViewModel: StoryViewModel = viewModel(factory = ViewModelFactory(storyDao))

    val notesQuery = remember { mutableStateOf("") }
    val notes = storyViewModel.story.observeAsState()

    ListStory(
        notes = notes.value.orPlaceHolderList(),
        query = notesQuery,
        navController
    )
}

@Composable
fun ListStory(
    notes: List<Story>,
    query: MutableState<String>,
    navController: NavController
) {
    LazyColumn {
        val queriedNotes = if (query.value.isEmpty()){
            notes
        } else {
            notes.filter { it.note.contains(query.value) || it.title.contains(query.value) }
        }
        itemsIndexed(queriedNotes) { _, note ->
            StoryListItem(
                note,
                navController = navController
            )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StoryListItem(
    story: Story,
    navController: NavController
) {

    return Box(modifier = Modifier
        .height(120.dp)
        .clip(RoundedCornerShape(12.dp))) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .fillMaxWidth()
                .height(120.dp)
                .combinedClickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(bounded = false),
                    onClick = {
                        if (story.id != null) {
                            navController.navigate(Screen.Detail.createRoute(story.id))
                        }
                    },
                )

        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ){
                if (!story.imageUri.isNullOrEmpty()){
                    Image(
                        painter = rememberAsyncImagePainter(
                            ImageRequest
                                .Builder(LocalContext.current)
                                .data(data = Uri.parse(story.imageUri))
                                .build()
                        ),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth(0.3f)
                            .fillMaxHeight(),
                        contentScale = ContentScale.Crop
                    )
                }

                Column {
                    Text(
                        text = story.title,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 12.dp),
                        maxLines = 2
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = getDayFormat(story.dateUpdated),
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                }
            }

        }
    }
}
