package es.ulpgc.pamn.pector.screens.pasapalabra

import es.ulpgc.pamn.pector.data.User
import es.ulpgc.pamn.pector.screens.test.TestGameState

sealed class PasapalabraState {
    object Playing: PasapalabraState()
    data class EndGame(val correctAnswers: Int, val incorrectAnswers: Int, val unanswered: Int, val points: Int, val userBeforeUpdate: User, val exception: Exception? = null): PasapalabraState()
}