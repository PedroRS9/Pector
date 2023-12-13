package es.ulpgc.pamn.pector.data

sealed class Result{
    data class Success(val boolean: Boolean): Result()
    data class Error(val exception: Exception): Result()
    object Loading: Result()
    data class LoginSuccess(val user: User): Result()
    data class ImageSuccess(val bytes: ByteArray): Result()
    data class QuestionSuccess(val questions: List<Question>): Result()
    data class LeaderboardSuccess(val leaderboard: List<TopScore>): Result()
    data class PasapalabraQuestionsSuccess(val questions: List<WordItem>): Result()

}
