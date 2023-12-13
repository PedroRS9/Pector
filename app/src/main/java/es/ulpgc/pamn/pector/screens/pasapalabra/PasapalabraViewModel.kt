package es.ulpgc.pamn.pector.screens.pasapalabra

import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import es.ulpgc.pamn.pector.data.FirebasePasapalabraQuestionsStackRepository
import es.ulpgc.pamn.pector.navigation.AppNavigation
import es.ulpgc.pamn.pector.navigation.AppScreens
import java.text.Normalizer
import es.ulpgc.pamn.pector.data.WordItem
import es.ulpgc.pamn.pector.data.Result
import es.ulpgc.pamn.pector.global.SharedRepositoryInstance

class PasapalabraViewModel : ViewModel() {

    private val repository =  FirebasePasapalabraQuestionsStackRepository()
    private var _isLoading = MutableLiveData<Boolean>(true)
    val isLoading: LiveData<Boolean> = _isLoading

    val currentAnswer = mutableStateOf("")
    val roscoWords = MutableLiveData<List<WordItem>>()
    val currentIndex = mutableStateOf(0)
    val wordStates = mutableStateOf<Map<String, WordAnswerState>>(emptyMap())

    private val _navigationEvent = MutableLiveData<NavigationEvent>()
    val navigationEvent: LiveData<NavigationEvent> = _navigationEvent

    private val totalTime = 20000L // Tiempo total en milisegundos
    val currentTime = mutableStateOf(totalTime / 1000) // Tiempo actual en segundos
    private var timer: CountDownTimer? = null

    val recognizedText: LiveData<String> = SharedRepositoryInstance.repository.getRecognizedText()

    init {
        SharedRepositoryInstance.repository.getRecognizedText().observeForever { newText ->
            onAnswerChanged(newText)
        }
        loadInitialData()
    }
    private fun loadInitialData() {
        repository.getRandomQuestions { result ->
            when (result) {
                is Result.PasapalabraQuestionsSuccess -> {
                    roscoWords.postValue(result.questions)
                    println(roscoWords.value)
                    observeRoscoWords()
                }

                else -> {}
            }
        }
    }
    private fun observeRoscoWords() {
        roscoWords.observeForever { words ->
            if (words != null) {
                println(words)
                initializeWordStates()
                startTimer()
                _isLoading.postValue(false)
            }
        }
    }

    private fun startTimer() {
        timer = object : CountDownTimer(totalTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // Actualiza el estado con el tiempo restante en segundos
                currentTime.value = millisUntilFinished / 1000
            }

            override fun onFinish() {
                // El tiempo se ha agotado, maneja el evento aquí
                currentTime.value = 0
                onTimeUp()
            }
        }.start()
    }

    private fun onTimeUp() {
        finalizeGame()
    }
    fun finalizeGame() {
        val correctAnswers = wordStates.value.count { it.value == WordAnswerState.Correct }
        val incorrectAnswers = wordStates.value.count { it.value == WordAnswerState.Incorrect }
        val unanswered = wordStates.value.count { it.value == WordAnswerState.Unanswered }

        // Cálculo de puntos, asegurándose de que no sean negativos
        val rawPoints = correctAnswers * 10 - incorrectAnswers * 5
        val points = rawPoints.coerceAtLeast(0)

        val route = AppScreens.PasapalabraEndScreen.createRoute(correctAnswers, incorrectAnswers, unanswered, points)
        _navigationEvent.value = NavigationEvent.Navigate(route)
    }

    override fun onCleared() {
        super.onCleared()
        timer?.cancel() // Asegúrate de cancelar el timer cuando el ViewModel se destruya
    }

    private fun initializeWordStates() {
        val initialStates = roscoWords.value!!.associate { it.word to WordAnswerState.Unanswered }
        wordStates.value = initialStates
    }

    fun onAnswerChanged(answer: String) {
        val answer_micro = normalizeString(answer)
        if(answer_micro.equals("pasapalabra", ignoreCase = true)){
            onPasapalabra()
        }else{
            currentAnswer.value = answer
        }

    }

    fun onSubmitAnswer() {
        val currentWord = normalizeString(roscoWords.value!![currentIndex.value].word)
        val userAnswer = normalizeString(currentAnswer.value)
        val isCorrect = userAnswer.equals(currentWord, ignoreCase = true)
        val newState = if (isCorrect) WordAnswerState.Correct else WordAnswerState.Incorrect

        // Actualizar el estado de la palabra actual
        wordStates.value = wordStates.value.toMutableMap().apply {
            put(roscoWords.value!![currentIndex.value].word, newState)
        }
        if (wordStates.value.all { it.value != WordAnswerState.Unanswered }) {

            finalizeGame()
        }else{
            moveToNextWord()
        }

    }

    fun onPasapalabra() {
        moveToNextWord()
    }

    private fun moveToNextWord() {
        var nextIndex = (currentIndex.value + 1) % roscoWords.value!!.size
        // Bucle para encontrar la siguiente palabra no respondida y no pasada
        while (wordStates.value[roscoWords.value!![nextIndex].word] != WordAnswerState.Unanswered) {
            nextIndex = (nextIndex + 1) % roscoWords.value!!.size
        }
        currentIndex.value = nextIndex
        currentAnswer.value = ""
    }
    private fun normalizeString(input: String): String {
        val normalized = Normalizer.normalize(input, Normalizer.Form.NFD)
        val accentsRemoved = normalized.replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")
        return accentsRemoved.trim()
    }
}

enum class WordAnswerState {
    Correct, Incorrect, Unanswered
}
sealed class NavigationEvent {
    object NavigateToEndScreen : NavigationEvent()
    data class Navigate(val route: String) : NavigationEvent()
}



