package com.whyaji.journey.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.whyaji.journey.model.Story

@Dao
interface StoryDao {
    @Query("SELECT * FROM stories WHERE stories.id=:id")
    suspend fun getStoryById(id: Int): Story?

    @Query("SELECT * FROM stories ORDER BY dateUpdated DESC")
    fun getStories(): LiveData<List<Story>>

    @Delete
    fun deleteStory(story: Story): Int

    @Insert
    fun addStory(story: Story)
}