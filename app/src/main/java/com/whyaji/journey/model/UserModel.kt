package com.whyaji.journey.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel (
    var token: String? = null,
) : Parcelable