package com.example.pruebamemoria

import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.collections.toMutableList

class GameViewModel : ViewModel() {
    private val _winEvent = MutableLiveData<Boolean>()
    val winEvent: LiveData<Boolean> = _winEvent
    private val _cards = MutableLiveData<List<Card>>()
    val cards: LiveData<List<Card>> = _cards

    private val _moves = MutableLiveData<Int>()
    val moves: LiveData<Int> = _moves

    private var firstSelectedCardIndex: Int? = null
    private var lockBoard = false

    init {
        startGame()
    }

    fun startGame() {
        val cardValues = (1..8).toList()  // 8 pares (16 cartas)
        val shuffledCards = (cardValues + cardValues)
            .shuffled()
            .map { Card(it) }

        _cards.value = shuffledCards
        _moves.value = 0
        firstSelectedCardIndex = null
        lockBoard = false
    }

    fun flipCard(index: Int) {
        if (lockBoard) return

        val currentCards = _cards.value?.toMutableList() ?: return
        val selectedCard = currentCards[index]

        if (selectedCard.isFaceUp || selectedCard.isMatched) return

        selectedCard.isFaceUp = true

        if (firstSelectedCardIndex == null) {
            firstSelectedCardIndex = index
        } else {
            _moves.value = _moves.value?.plus(1)

            val firstCard = currentCards[firstSelectedCardIndex!!]

            if (firstCard.id == selectedCard.id) {
                firstCard.isMatched = true
                selectedCard.isMatched = true
                firstSelectedCardIndex = null
                checkWin(currentCards)
            } else {
                lockBoard = true
                Handler(Looper.getMainLooper()).postDelayed({
                    firstCard.isFaceUp = false
                    selectedCard.isFaceUp = false
                    firstSelectedCardIndex = null
                    lockBoard = false
                    _cards.value = currentCards
                }, 1000)
            }
        }

        _cards.value = currentCards
    }

    private fun checkWin(cards: List<Card>) {
        if (cards.all { it.isMatched }) {
            _winEvent.value = true
        }
    }
}