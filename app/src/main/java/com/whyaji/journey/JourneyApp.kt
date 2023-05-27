package com.whyaji.journey

import android.app.Application
import androidx.room.Room
import com.whyaji.journey.data.StoryDao
import com.whyaji.journey.data.StoryDatabase
import com.whyaji.journey.util.Constants

class JourneyApp : Application() {
    private var db: StoryDatabase? = null

    companion object {
        private var instance: JourneyApp? = null

        fun getDao(): StoryDao {
            return instance?.getDb()?.StoryDao()
                ?: throw IllegalStateException("JourneyApp instance is not initialized")
        }

        fun getInstance(): JourneyApp {
            return instance ?: throw IllegalStateException("JourneyApp instance is not initialized")
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    fun getDb(): StoryDatabase {
        return db ?: synchronized(this) {
            db ?: Room.databaseBuilder(
                applicationContext,
                StoryDatabase::class.java,
                Constants.DATABASE_NAME
            ).fallbackToDestructiveMigration() // remove in prod
                .build().also { db = it }
        }
    }
}
