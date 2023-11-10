package es.ulpgc.pamn.pector.data

sealed class Result{
    data class Success(val user: User): Result()
    data class Error(val exception: Exception): Result()
}
