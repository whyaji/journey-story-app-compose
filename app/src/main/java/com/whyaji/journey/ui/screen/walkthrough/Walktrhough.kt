package com.whyaji.journey.ui.screen.walkthrough

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.whyaji.journey.JourneyApp
import com.whyaji.journey.model.Story
import com.whyaji.journey.ui.navigation.Graph
import com.whyaji.journey.util.DatastorePreference
import com.whyaji.journey.util.WalkthroughItems
import com.whyaji.journey.viewmodel.StoryViewModel
import com.whyaji.journey.viewmodel.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.random.Random

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Walkthrough(navController: NavController, dataStorePreference: DatastorePreference) {
    val items = WalkthroughItems.getData()
    val scope = rememberCoroutineScope()
    val pageState = rememberPagerState()

    Column(modifier = Modifier.fillMaxSize()) {
        TopSection(
            pageState.currentPage < items.size - 1,
            onSkipClick = {
                if (pageState.currentPage + 1 < items.size) scope.launch {
                    pageState.scrollToPage(items.size - 1)
                }
            }
        )

        HorizontalPager(
            count = items.size,
            state = pageState,
            modifier = Modifier
                .fillMaxHeight(0.9f)
                .fillMaxWidth()
        ) { page ->
            WalkthroughItem(items = items[page], page, navController, dataStorePreference)
        }

        BottomSection(
            size = items.size,
            index = pageState.currentPage,
            onNextClick = {
                if (pageState.currentPage < items.size - 1) scope.launch {
                    pageState.animateScrollToPage(pageState.currentPage + 1)
                }
            },
            onPrevClick = {
                if (pageState.currentPage + 1 > 1) scope.launch {
                    pageState.animateScrollToPage(pageState.currentPage - 1)
                }
            }
        )
    }
}

@Composable
fun TopSection(visible: Boolean, onSkipClick: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(horizontal = 15.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            TextButton(
                onClick = onSkipClick,
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(text = "Skip", color = MaterialTheme.colorScheme.onBackground)
            }
        }
    }
}

@Composable
fun BottomSection(size: Int, index: Int, onNextClick: () -> Unit = {}, onPrevClick: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Previous Button
            IconButton(
                onClick = {
                    if (index > 0) {
                        onPrevClick.invoke()
                    }
                },
                enabled = index > 0
            ) {
                AnimatedVisibility(
                    index > 0,
                    enter = fadeIn(),
                    exit = fadeOut()) {
                    Icon(
                        imageVector = Icons.Outlined.KeyboardArrowLeft,
                        contentDescription = null
                    )
                }
            }

            // Indicators
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                repeat(size) {
                    Indicator(isSelected = it == index)
                }
            }

            // Next Button
            IconButton(
                onClick = {
                    if (index < size - 1) {
                        onNextClick.invoke()
                    }
                },
                enabled = index < size - 1
            ) {
                AnimatedVisibility(
                    visible = index < size - 1,
                    enter = fadeIn(),
                    exit = fadeOut()) {
                    Icon(
                        imageVector = Icons.Outlined.KeyboardArrowRight,
                        contentDescription = null
                    )
                }
            }
        }
    }
}

@Composable
fun Indicator(isSelected: Boolean) {
    val width = animateDpAsState(
        targetValue = if (isSelected) 25.dp else 10.dp,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
    )

    Box(
        modifier = Modifier
            .height(10.dp)
            .width(width.value)
            .clip(CircleShape)
            .background(
                color = if (isSelected) MaterialTheme.colorScheme.primary else Color(0XFFF8E2E7)
            )
    ) {

    }
}

@Composable
fun WalkthroughItem(
    items: WalkthroughItems,
    page: Int,
    navController: NavController,
    dataStorePreference: DatastorePreference
) {
    val storyDao = JourneyApp.getInstance().getDb().StoryDao()
    val storyViewModel: StoryViewModel = viewModel(factory = ViewModelFactory(storyDao))

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = items.image),
            contentDescription = "Image1",
            modifier = Modifier.padding(start = 50.dp, end = 50.dp)
        )

        Spacer(modifier = Modifier.height(25.dp))

        Text(
            text = stringResource(id = items.title),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            letterSpacing = 1.sp,
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(id = items.desc),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Light,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(10.dp),
            letterSpacing = 1.sp,
        )

        val token = remember { mutableStateOf("") }

        if (page == 2) {
            Box (
                modifier = Modifier.height(100.dp),
                contentAlignment = Alignment.BottomCenter
            ){
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp),
                    onClick = {
                        applyDummy(storyViewModel)
                        val newToken = "Your Token Value"
                        CoroutineScope(Dispatchers.IO).launch {
                            dataStorePreference.setToken(newToken)
                            token.value = newToken
                        }
                        navController.popBackStack()
                        navController.navigate(Graph.HOME)
                    },
                ) {
                    Text(
                        text = "Get Started",
                        fontSize = 20.sp
                    )
                }
            }
        } else {
            Spacer(
                modifier = Modifier.height(100.dp),
            )
        }
    }
}

fun applyDummy(storyViewModel: StoryViewModel) {
    val dummyStories = listOf(
        Story(
            id = 1,
            note = "A heartwarming tale of friendship and perseverance.",
            title = "The Journey of Hope",
            dateUpdated = "2023-05-27 06:21:28",
            imageUri = "https://picsum.photos/seed/${Random.nextInt()}/300/200"
        ),
        Story(
            id = 2,
            note = "A thrilling mystery set in a small town with dark secrets.",
            title = "Shadows in the Moonlight",
            dateUpdated = "2023-05-27 12:45:10",
            imageUri = "https://picsum.photos/seed/${Random.nextInt()}/300/200"
        ),
        Story(
            id = 3,
            note = "An epic fantasy adventure filled with magic and danger.",
            title = "The Chronicles of Eldoria",
            dateUpdated = "2023-05-25 21:30:55",
            imageUri = "https://picsum.photos/seed/${Random.nextInt()}/300/200"
        ),
        Story(
            id = 4,
            note = "A poignant story of love and loss set during World War II.",
            title = "Whispers of the Past",
            dateUpdated = "2023-05-24 15:10:20",
            imageUri = "https://picsum.photos/seed/${Random.nextInt()}/300/200"
        ),
        Story(
            id = 5,
            note = "A gripping psychological thriller that will keep you on the edge of your seat.",
            title = "In the Shadows of Doubt",
            dateUpdated = "2023-05-23 08:30:15",
            imageUri = "https://picsum.photos/seed/${Random.nextInt()}/300/200"
        ),
        Story(
            id = 6,
            note = "A heartrending tale of a family torn apart by tragedy and reunited by forgiveness.",
            title = "Threads of Love",
            dateUpdated = "2023-05-23 17:45:40",
            imageUri = "https://picsum.photos/seed/${Random.nextInt()}/300/200"
        ),
        Story(
            id = 7,
            note = "A gripping courtroom drama with unexpected twists and turns.",
            title = "Beyond Reasonable Doubt",
            dateUpdated = "2023-05-21 11:20:35",
            imageUri = "https://picsum.photos/seed/${Random.nextInt()}/300/200"
        ),
        Story(
            id = 8,
            note = "A captivating historical fiction set in ancient Rome.",
            title = "The Roman Conspiracy",
            dateUpdated = "2023-05-20 09:15:50",
            imageUri = "https://picsum.photos/seed/${Random.nextInt()}/300/200"
        ),
        Story(
            id = 9,
            note = "A heartwarming story of a dog's unwavering loyalty and devotion.",
            title = "Forever Faithful",
            dateUpdated = "2023-05-20 09:10:50",
            imageUri = "https://picsum.photos/seed/${Random.nextInt()}/300/200"
        ),
        Story(
            id = 10,
            note = "An inspiring tale of a young artist's journey to self-discovery.",
            title = "Brushstrokes of Dreams",
            dateUpdated = "2023-05-20 06:15:50",
            imageUri = "https://picsum.photos/seed/${Random.nextInt()}/300/200"
        ),
        Story(
            id = 11,
            note = "A captivating tale of survival in the wilderness.",
            title = "Wilderness Adventures",
            dateUpdated = "2023-05-17 18:35:50",
            imageUri = "https://picsum.photos/seed/${Random.nextInt()}/300/200"
        ),
        Story(
            id = 12,
            note = "A heartwarming story of second chances and new beginnings.",
            title = "The Road to Redemption",
            dateUpdated = "2023-05-17 10:15:30",
            imageUri = "https://picsum.photos/seed/${Random.nextInt()}/300/200"
        ),
        Story(
            id = 13,
            note = "A gripping psychological thriller with a shocking twist.",
            title = "Mind Games",
            dateUpdated = "2023-05-17 14:20:45",
            imageUri = "https://picsum.photos/seed/${Random.nextInt()}/300/200"
        ),
        Story(
            id = 14,
            note = "An enchanting love story set in a magical world.",
            title = "The Spellbinding Romance",
            dateUpdated = "2023-05-16 16:40:55",
            imageUri = "https://picsum.photos/seed/${Random.nextInt()}/300/200"
        ),
        Story(
            id = 15,
            note = "A thrilling adventure through ancient ruins and hidden treasures.",
            title = "The Quest for Legends",
            dateUpdated = "2023-05-16 11:25:20",
            imageUri = "https://picsum.photos/seed/${Random.nextInt()}/300/200"
        )
    )

    dummyStories.forEach { dummyStory ->
        storyViewModel.addStoryDummy(dummyStory)
    }
}
