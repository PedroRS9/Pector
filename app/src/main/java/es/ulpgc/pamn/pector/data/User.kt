package es.ulpgc.pamn.pector.data

import kotlin.math.pow

class User(
    private val name: String,
    private val password: String,
    private val email: String,
    private var pictureURL: String? = null,
    // we show the default picture R.drawable.default_profile_pic if the user has not uploaded one
    private var picture: ByteArray? = null,
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
    fun getPicture(): ByteArray? = picture
    fun setPicture(picture: ByteArray) {
        this.picture = picture
    }
    fun getLevel() = level
    fun getXp() = xp
    fun setXp(xp: Int) {
        this.xp = xp
    }

    fun getMinimumXpForCurrentLevel(): Int{
        return ( (level-1) / 0.1).pow(2.0).toInt()
    }
    fun getXpToNextLevel(): Int{
        return (level / 0.1).pow(2.0).toInt()
    }

    fun calculateXpPercentage(): Float {
        val minXP = getMinimumXpForCurrentLevel()
        val maxXP = getXpToNextLevel()
        val userXp = getXp()
        if (userXp >= maxXP) return 1.0f // 100%
        if (userXp <= minXP) return 0.0f // 0%
        return (userXp - minXP).toFloat() / (maxXP - minXP).toFloat()
    }


    fun hasProfilePicture(): Boolean{
        return picture != null
    }


    /**
     * Añade experiencia al usuario y devuelve si ha subido de nivel.
     *
     * @param xp La cantidad de experiencia a añadir.
     * @return true si ha subido de nivel, false en caso contrario.
     */
    fun addXp(xp: Int): Boolean{
        this.xp += xp
        if (this.xp >= getXpToNextLevel()){
            level++
            return true
        }
        return false
    }

    fun copy(): User {
        return User(name, password, email, pictureURL, picture, level, xp)
    }

}