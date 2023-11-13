package es.ulpgc.pamn.pector.extensions

fun String.isValidUsername(): Boolean{
    val regex = "^[a-zA-Z0-9._-]{3,30}$".toRegex()
    return regex.matches(this)
}

fun String.isValidEmail(): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String.isValidPassword(): Boolean {
    return this.length >= 6
}