package com.lmalecic.lovefinderzz.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lmalecic.lovefinderzz.R
import com.lmalecic.lovefinderzz.entity.Gender

val Gender.icon_filled: Int
    get() = when (this) {
        Gender.MALE -> R.drawable.ic_gender_male
        Gender.FEMALE -> R.drawable.ic_gender_female
        Gender.GENDERLESS -> R.drawable.ic_gender_genderless
        Gender.UNKNOWN -> R.drawable.ic_gender_unknown
    }

fun Modifier.pageContentPadding() : Modifier = this.padding(24.dp, 12.dp, 24.dp, 0.dp)