package es.ulpgc.pamn.pector.data


data class TopScore(
    var profilePicture: ByteArray? = null,
    val username: String,
    val score: Int,
    val date: String,
    val gameType: String
)
