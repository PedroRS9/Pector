package es.ulpgc.pamn.pector.data

sealed class Result{
    data class Success(val boolean: Boolean): Result()
    data class Error(val exception: Exception): Result()
}
