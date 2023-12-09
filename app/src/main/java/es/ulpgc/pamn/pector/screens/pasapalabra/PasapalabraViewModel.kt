package es.ulpgc.pamn.pector.screens.pasapalabra

import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import androidx.compose.runtime.mutableStateOf

class PasapalabraViewModel : ViewModel() {
    private val wordItems = listOf(
        WordItem("CASA", "Lugar donde vives"),
        WordItem("PERRO", "Animal doméstico que ladra"),
        WordItem("GATO", "Animal doméstico que maúlla"),
        WordItem("COCINA", "Lugar donde se preparan los alimentos"),
        WordItem("ARBOL", "Planta grande con tronco y ramas")
    )

    val currentAnswer = mutableStateOf("")
    val roscoWords = MutableLiveData<List<WordItem>>(wordItems)
    val currentIndex = mutableStateOf(0)
    val wordStates = mutableStateOf<Map<String, WordAnswerState>>(emptyMap())

    init {
        initializeWordStates()
    }

    private fun initializeWordStates() {
        val initialStates = roscoWords.value!!.associate { it.word to WordAnswerState.Unanswered }
        wordStates.value = initialStates
    }

    fun onAnswerChanged(answer: String) {
        currentAnswer.value = answer
    }

    fun onSubmitAnswer() {
        val currentWord = roscoWords.value!![currentIndex.value].word
        val isCorrect = currentAnswer.value.equals(currentWord, ignoreCase = true)
        val newState = if (isCorrect) WordAnswerState.Correct else WordAnswerState.Incorrect

        wordStates.value = wordStates.value.toMutableMap().apply { put(currentWord, newState) }

        moveToNextWord()
    }

    private fun moveToNextWord() {
        currentIndex.value = (currentIndex.value + 1) % roscoWords.value!!.size
        currentAnswer.value = ""
    }
}

enum class WordAnswerState {
    Correct, Incorrect, Unanswered
}



