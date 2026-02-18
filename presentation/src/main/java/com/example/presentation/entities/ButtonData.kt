package com.example.presentation.entities

import androidx.annotation.DrawableRes

data class ButtonData(
    @DrawableRes val iconRes: Int,
    val onClick: () -> Unit
)