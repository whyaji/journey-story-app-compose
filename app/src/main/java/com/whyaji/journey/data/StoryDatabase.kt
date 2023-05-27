package com.whyaji.journey.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.whyaji.journey.model.Story

@Database(
    entities = [
    Story::class], version = 1
)
abstract class StoryDatabase : RoomDatabase() {
    abstract fun StoryDao(): StoryDao
}