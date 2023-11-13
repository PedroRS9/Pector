package es.ulpgc.pamn.pector.data

class User(
    private val name: String,
    private val password: String,
    private val email: String,
    private var pictureURL: String? = null
) {
    fun getName() = name;
    fun getPassword() = password;
    fun getEmail() = email;
    fun getPictureURL() = pictureURL
}