package es.ulpgc.pamn.pector.global

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import es.ulpgc.pamn.pector.data.User

class UserGlobalConf {

    private val _currentUser = MutableLiveData<User?>()
    val currentUser: LiveData<User?> = _currentUser
    fun setUser(user: User) {
        _currentUser.value = user
    }

}
