package es.ulpgc.pamn.pector.data

class User(
    private val name: String,
    private val password: String,
    private val email: String,
    private var pictureURL: String? = null,
    private var level: Int = 1,
    private var xp: Int = 0
) {
    fun getName() = name;
    fun getPassword() = password;
    fun getEmail() = email;
    fun getPictureURL() = pictureURL
    fun setPictureURL(pictureURL: String) {
        this.pictureURL = pictureURL
    }
    fun getLevel() = level
    fun getXp() = xp
}