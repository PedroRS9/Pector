package es.ulpgc.pamn.pector.data

sealed class Result{
    data class Success(val boolean: Boolean): Result()
    data class Error(val exception: Exception): Result()
    data class LoginSuccess(val user: User): Result()
    data class ImageSuccess(val bytes: ByteArray): Result()
}
