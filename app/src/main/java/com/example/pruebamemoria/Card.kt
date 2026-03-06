package com.example.pruebamemoria

import androidx.annotation.DrawableRes
data class Card(
    @DrawableRes val imageResId: Int,
    var isFaceUp: Boolean = false,
    var isMatched: Boolean = false
)
