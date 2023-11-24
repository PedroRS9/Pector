package es.ulpgc.pamn.pector.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import com.google.firebase.auth.FirebaseAuth
import es.ulpgc.pamn.pector.data.FirebaseUserRepository
import es.ulpgc.pamn.pector.data.UserRepository
import es.ulpgc.pamn.pector.data.Result
import es.ulpgc.pamn.pector.data.User
import java.lang.Exception

class LoginViewModel : ViewModel() {

    private val userRepository: UserRepository = FirebaseUserRepository()
    private val _loginState = MutableLiveData<Result>()
    val loginState: LiveData<Result> = _loginState

    fun onLogin(usernameOrEmail: String, password: String) {
        if (usernameOrEmail.isBlank() || password.isBlank()) {
            _loginState.value = Result.Error(Exception("Se requieren nombre de usuario/email y contraseña"))
            return
        }

        val isEmail = usernameOrEmail.contains("@")

        val userQueryCallback: (User?) -> Unit = { user ->
            if (user == null) {
                _loginState.postValue(Result.Error(Exception("Usuario no encontrado")))
            } else {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(user.getEmail(), password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            _loginState.postValue(Result.Success(true))
                        } else {
                            _loginState.postValue(Result.Error(Exception("Fallo de autenticación")))
                        }
                    }
            }
        }

        if (isEmail) {
            userRepository.findUserByEmail(usernameOrEmail, userQueryCallback)
        } else {
            userRepository.findUserByUsername(usernameOrEmail, userQueryCallback)
        }
    }

    fun clearError() {
        _loginState.value = null
    }
}
