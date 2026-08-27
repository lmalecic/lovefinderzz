package com.lmalecic.lovefinderzz.ui

import com.lmalecic.lovefinderzz.R
import com.lmalecic.lovefinderzz.entity.Gender

val Gender.icon_filled: Int
    get() = when (this) {
        Gender.MALE -> R.drawable.ic_gender_male
        Gender.FEMALE -> R.drawable.ic_gender_female
        Gender.GENDERLESS -> R.drawable.ic_gender_genderless
        Gender.UNKNOWN -> R.drawable.ic_gender_unknown
    }
