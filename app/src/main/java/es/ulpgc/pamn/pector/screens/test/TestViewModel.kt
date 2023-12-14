package es.ulpgc.pamn.pector.screens.test

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.ulpgc.pamn.pector.data.FirebaseQuestionRepository
import es.ulpgc.pamn.pector.data.FirebaseUserRepository
import es.ulpgc.pamn.pector.data.Question
import es.ulpgc.pamn.pector.data.Result
import es.ulpgc.pamn.pector.data.User
import es.ulpgc.pamn.pector.global.UserGlobalConf
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TestViewModel() : ViewModel() {
    private val questionRepository = FirebaseQuestionRepository()
    private val userRepository = FirebaseUserRepository()
    // we initialize the state with the loading state
    private val _gameState = MutableLiveData<TestGameState>(TestGameState.Loading)
    val gameState: LiveData<TestGameState> = _gameState

    private val questions = ArrayList<Question>()
    private var currentQuestionIndex = 0
    private var correctAnswers = 0

    private lateinit var userGlobalConf: UserGlobalConf

    fun establishUser(userGlobalConf: UserGlobalConf){
        this.userGlobalConf = userGlobalConf
    }

    fun getQuestions(numberOfQuestions: Int, category: String) {
        questionRepository.getRandomQuestions(10, category) { result: Result ->
            if (result is Result.QuestionSuccess) {
                questions.addAll(result.questions)
                val currentQuestion = questions[currentQuestionIndex]
                _gameState.value = TestGameState.AnsweringQuestion(currentQuestion)
            }
        }
    }

    fun onOptionSelected(option: String) {
        // ponemos un delay de 1 segundo para causar tensión
        viewModelScope.launch{
            _gameState.value = TestGameState.CheckingIfOptionIsCorrect(questions[currentQuestionIndex], option)
            delay(1000)
            val correctOption = questions[currentQuestionIndex].correctOption
            _gameState.value = TestGameState.ShowCorrectOption(questions[currentQuestionIndex], option, correctOption)
            delay(3000)
            if (option == correctOption) {
                correctAnswers++
            }
            currentQuestionIndex++
            if (currentQuestionIndex < questions.size) {
                val currentQuestion = questions[currentQuestionIndex]
                _gameState.value = TestGameState.AnsweringQuestion(currentQuestion)
            } else {
                val earnedPoints = correctAnswers * 7
                val userBeforeUpdate = userGlobalConf.currentUser.value!!.copy()
                userGlobalConf.currentUser.value!!.addXp(earnedPoints)
                _gameState.value = TestGameState.EndGame(correctAnswers, questions.size, earnedPoints, userBeforeUpdate)
                userRepository.updateUser(userGlobalConf.currentUser.value!!){result ->
                    if (result is Result.Error){
                        _gameState.value = TestGameState.EndGame(correctAnswers, questions.size, earnedPoints, userBeforeUpdate, result.exception)
                        return@updateUser
                    }
                }
            }
        }
    }


}