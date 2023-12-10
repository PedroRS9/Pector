package es.ulpgc.pamn.pector.data

import es.ulpgc.pamn.pector.extensions.equalsIgnoreCaseAndAccents
import java.util.Locale


data class Question(val category: String, val question: String, val correctOption: String, val incorrectOptions: Array<String>) {
    val options: Array<String>

    init {
        options = randomizeOptions()
    }

    private fun randomizeOptions(): Array<String> {
        // returns the options in a random order
        val options = Array(incorrectOptions.size + 1) { "" }
        options[0] = correctOption
        for (i in 1 until options.size) {
            options[i] = incorrectOptions[i - 1]
        }
        options.shuffle()
        return options
    }

    fun isAnswerCorrect(answer: String): Boolean {
        // checks if the answer is correct ignoring case and accents
        return answer.equalsIgnoreCaseAndAccents(correctOption)
    }
}
