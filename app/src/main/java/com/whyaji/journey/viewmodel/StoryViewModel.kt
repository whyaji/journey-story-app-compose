package com.whyaji.journey.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.whyaji.journey.data.StoryDao
import com.whyaji.journey.model.Story
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StoryViewModel(
    private val storyDao: StoryDao,
) : ViewModel() {

    val story: LiveData<List<Story>> = storyDao.getStories()


    fun deleteStory(story: Story) {
        viewModelScope.launch(Dispatchers.IO){
            storyDao.deleteStory(story)
        }
    }

    fun addStory(title: String, note: String, image: String? = null) {
        viewModelScope.launch(Dispatchers.IO){
            storyDao.addStory(Story(title = title, note = note, imageUri = image))
        }
    }

    fun addStoryDummy(story: Story) {
        viewModelScope.launch(Dispatchers.IO){
            storyDao.addStory(story)
        }
    }

    suspend fun getStory(storyId : Int) : Story? {
        return storyDao.getStoryById(storyId)
    }

}

class ViewModelFactory(
    private val db: StoryDao,
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return  StoryViewModel(
            storyDao = db,
        ) as T
    }

}