package es.ulpgc.pamn.pector.data

import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore

class FirebaseQuestionRepository : QuestionRepository {
    private val database = Firebase.firestore
    override fun getRandomQuestions(numberOfQuestions: Int, category: String?, weWantDefinitions: Boolean, callback: (Result) -> Unit) {
        if(numberOfQuestions <= 0) callback(Result.Error(Exception("El número de preguntas debe ser mayor que 0.")))
        val query: Query = when (weWantDefinitions) {
            true -> database.collection("questions")
                .whereEqualTo("isDefinition", true)
                .orderBy("random")
                .limit(numberOfQuestions.toLong())
            false -> database.collection("questions")
                .orderBy("random")
                .limit(numberOfQuestions.toLong())
        }
        query.get()
            .addOnCompleteListener{ task ->
                if(!task.isSuccessful) callback(Result.Error(Exception("No se pudo obtener preguntas de la base de datos")))
                val questionsDocuments: MutableList<DocumentSnapshot> = task.result.documents
                val questions: MutableList<Question> = questionsDocuments
                    .map {
                        Question(
                            category = it.getString("category") ?: "",
                            question = it.getString("question") ?: "",
                            correctOption = it.getString("correctOption") ?: "",
                            incorrectOptions = it.get("incorrectOptions") as Array<String>
                        )
                    }
                    .toMutableList()
                callback(Result.QuestionSuccess(questions))
            }
    }
}