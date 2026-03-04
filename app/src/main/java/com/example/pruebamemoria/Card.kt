package com.example.pruebamemoria

data class Card(
    val id: Int,          // Identificador del par
    var isFaceUp: Boolean = false,
    var isMatched: Boolean = false
)
