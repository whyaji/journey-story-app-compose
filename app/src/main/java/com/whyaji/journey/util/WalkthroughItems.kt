package com.whyaji.journey.util

import com.whyaji.journey.R

class WalkthroughItems (
    val image: Int,
    val title: Int,
    val desc: Int
    ) {
    companion object {
        fun getData(): List<WalkthroughItems> {
            return listOf(
                WalkthroughItems(
                    R.drawable.img_walthrough_1,
                    R.string.walkthroughTitle1,
                    R.string.walkthroughText1
                ),
                WalkthroughItems(
                    R.drawable.img_walthrough_2,
                    R.string.walktroughTitle2,
                    R.string.walktroughText2
                ),
                WalkthroughItems(
                    R.drawable.img_walthrough_3,
                    R.string.walktroughTitle3,
                    R.string.walktroughText3
                )
            )
        }
    }
}