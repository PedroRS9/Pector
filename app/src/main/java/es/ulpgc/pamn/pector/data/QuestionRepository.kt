package es.ulpgc.pamn.pector.data

interface QuestionRepository {
    fun getRandomQuestions(numberOfQuestions: Int, category: String?, weWantDefinitions: Boolean = false, callback: (Result) -> Unit)
}