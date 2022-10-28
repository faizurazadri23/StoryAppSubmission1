package com.faizurazadri.storyappsubmission1.utils

import com.faizurazadri.storyappsubmission1.data.source.model.ListStoryItem

object DataDummy {

    fun generateDummyStoriesEntities() : List<ListStoryItem> {
        val storyList = ArrayList<ListStoryItem>()

        for (i in 0..10){
            val stories = ListStoryItem(
                "Title $i",
                "Story $i",
                "Description Story $i",
                0.24242232,
                0.123232,
                "https://www.google.com",
                "22-09-23 24:23:21"
            )

            storyList.add(stories)
        }

        return storyList
    }
}