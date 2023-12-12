package es.ulpgc.pamn.pector.data

import kotlin.math.pow

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

    fun getXpToNextLevel(): Int{
        return (level / 0.1).pow(2.0).toInt()
    }


    /**
     * Añade experiencia al usuario y devuelve si ha subido de nivel.
     *
     * @param xp La cantidad de experiencia a añadir.
     * @return true si ha subido de nivel, false en caso contrario.
     */
    fun addXp(xp: Int): Boolean{
        this.xp += xp
        if (xp >= getXpToNextLevel()){
            level++
            return true
        }
        return false
    }

}