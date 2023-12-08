package es.ulpgc.pamn.pector.extensions

import java.util.Locale

fun String.equalsIgnoreCaseAndAccents(other: String): Boolean {
    val regex = Regex("[^\\p{ASCII}]")
    val normalizedThis = this.replace(regex, "").lowercase(Locale.getDefault())
    val normalizedOther = other.replace(regex, "").lowercase(Locale.getDefault())
    return normalizedThis == normalizedOther
}