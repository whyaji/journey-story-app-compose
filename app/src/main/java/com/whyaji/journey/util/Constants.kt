package com.whyaji.journey.util

import com.whyaji.journey.model.Story

object Constants {
    const val TABLE_NAME = "stories"
    const val DATABASE_NAME = "storydatabase"

    fun List<Story>?.orPlaceHolderList(): List<Story> {
        fun placeHolderList(): List<Story> {
            return listOf(Story(id = 0, title = "No Story Found", note = "Please create a story.", dateUpdated = ""))
        }
        return if (!this.isNullOrEmpty()){
            this
        } else placeHolderList()
    }
    val storyDetailPlaceHolder = Story(note = "Cannot find story details", id = 0, title = "Cannot find story details")
}