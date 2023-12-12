package es.ulpgc.pamn.pector.screens.test

import es.ulpgc.pamn.pector.data.Question

sealed class TestGameState {
    object Loading: TestGameState()
    data class AnsweringQuestion(val question: Question): TestGameState()
    data class CheckingIfOptionIsCorrect(val question: Question, val option: String): TestGameState()
    data class ShowCorrectOption(val question: Question, val selectedOption: String, val correctOption: String): TestGameState()
    data class EndGame(val correctAnswers: Int, val totalQuestions: Int, val earnedPoints: Int, val exception: Exception? = null): TestGameState()

}