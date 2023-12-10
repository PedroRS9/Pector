package es.ulpgc.pamn.pector.data

interface PasapalabraQuestionsStackRepository {
    fun getRandomQuestions(callback: (Result) -> Unit)
}