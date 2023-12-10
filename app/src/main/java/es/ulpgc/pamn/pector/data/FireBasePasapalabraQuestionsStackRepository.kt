package es.ulpgc.pamn.pector.data

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlin.random.Random

class FirebasePasapalabraQuestionsStackRepository : PasapalabraQuestionsStackRepository {
    private val database = Firebase.firestore

    override fun getRandomQuestions(callback: (Result) -> Unit) {
        database.collection("pasapalabra_questions_stacks").get()
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    callback(Result.Error(Exception("No se pudo obtener preguntas de la base de datos")))
                    return@addOnCompleteListener
                }

                val randomQuestions = mutableListOf<WordItem>()
                for (document in task.result.documents) {
                    val questionsList = document.data?.get("contenido") as? List<Map<String, String>> ?: continue
                    if (questionsList.isNotEmpty()) {
                        // Seleccionar una pregunta aleatoria
                        val randomIndex = Random.nextInt(questionsList.size)
                        val randomQuestionMap = questionsList[randomIndex]
                        val wordItem = WordItem(
                            randomQuestionMap["word"] ?: "",
                            randomQuestionMap["description"] ?: ""
                        )
                        randomQuestions.add(wordItem)
                    }
                }
                callback(Result.PasapalabraQuestionsSuccess(randomQuestions))
            }
    }
}