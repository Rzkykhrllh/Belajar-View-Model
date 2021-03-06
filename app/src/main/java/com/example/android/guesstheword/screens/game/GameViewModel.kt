package com.example.android.guesstheword.screens.game

import android.os.CountDownTimer
import android.text.format.DateUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {

    // The current word
    val _word = MutableLiveData<String>()
    val word : LiveData<String> //cuma bisa get, gak bisa set
        get() = _word //jadi saat word diakses dari luar, dia bakal return nilai _word (yg dapat diubah di dalam)

    // The current score
    val _score = MutableLiveData<Int>()
    val score : LiveData<Int>
        get() = _score

    // The list of words - the front of the list is the next word to guess
    private lateinit var wordList: MutableList<String>

    //buat ngasih tau gameFragment kalau daftar kata sudah habis
    val _isGameFinish = MutableLiveData<Boolean>()
    val isGameFinish : LiveData<Boolean>
        get() = _isGameFinish

    //init konstanta timer
    companion object {
        // These represent different important times
        // This is when the game is over
        const val DONE = 0L
        // This is the number of milliseconds in a second
        const val ONE_SECOND = 1000L
        // This is the total time of the game
        const val COUNTDOWN_TIME = 5000L
    }

    //init timer
    private lateinit var timer: CountDownTimer

    //init variabel waktu sekarang
    val _currentTimer = MutableLiveData<String>()
    val currentTimer : LiveData<String>
        get() = _currentTimer

    init {
        Log.i("GameViewModel", "GameViewModel Created")
        resetList()

        //init nilai awal dari score dan word
        _score.value = 0
        _word.value = ""

        nextWord()

        //init objek timernya
        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {
            //parameternya 2, banyak waktu, sama waktu tiap kliknya

            override fun onTick(millisUntilFinished: Long) {
                _currentTimer.value = DateUtils.formatElapsedTime(millisUntilFinished).toString()
            }

            override fun onFinish() {
                _isGameFinish.value = true
            }
        }

        timer.start()

    }

    override fun onCleared() {
        super.onCleared()
        timer.cancel() //mematikan timer saat GameViewModel tidak digunakan lagi
        Log.i("GameViewModel", "GameViewModel Destroyed")
    }


    //Resets the list of words and randomizes the order
    private fun resetList() {
        wordList = mutableListOf(
                "queen",
                "hospital",
                "basketball",
                "cat",
                "change",
                "snail",
                "soup",
                "calendar",
                "sad",
                "desk",
                "guitar",
                "home",
                "railway",
                "zebra",
                "jelly",
                "car",
                "crow",
                "trade",
                "bag",
                "roll",
                "bubble"
        )
        wordList.shuffle()
    }


    // Moves to the next word in the list
    private fun nextWord() {
        //Select and remove a word from the list
        if (wordList.isEmpty()) {
            resetList()
        }
        _word.value = wordList.removeAt(0)
    }

    /** Methods for buttons presses **/
    fun onSkip() {
        _score.value = score.value?.minus(1)
        nextWord()
    }

    fun onCorrect() {
        _score.value = score.value?.plus(1)
        nextWord()
    }


}