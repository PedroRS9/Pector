package es.ulpgc.pamn.pector.screens.test

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import es.ulpgc.pamn.pector.data.FirebaseQuestionRepository
import es.ulpgc.pamn.pector.data.Result

class TestViewModel : ViewModel() {
    private val questionRepository = FirebaseQuestionRepository()
    private val _questionsState = MutableLiveData<Result>()
    val questionsState: MutableLiveData<Result> = _questionsState

    fun getQuestions(numberOfQuestions: Int, category: String) {
        questionRepository.getRandomQuestions(numberOfQuestions, category) { result: Result ->
            _questionsState.value = result
        }
    }
}