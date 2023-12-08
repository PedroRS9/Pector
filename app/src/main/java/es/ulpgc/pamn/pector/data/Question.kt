package es.ulpgc.pamn.pector.data

import es.ulpgc.pamn.pector.extensions.equalsIgnoreCaseAndAccents
import java.util.Locale

data class Question(val category: String, val question: String, val correctOption: String, val incorrectOptions: Array<String>){
    fun isAnswerCorrect(answer: String): Boolean{
        // checks if the answer is correct ignoring case and accents
        return answer.equalsIgnoreCaseAndAccents(correctOption)
    }
}