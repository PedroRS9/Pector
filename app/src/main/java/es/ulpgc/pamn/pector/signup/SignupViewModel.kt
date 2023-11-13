package es.ulpgc.pamn.pector.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import es.ulpgc.pamn.pector.data.FirebaseUserRepository
import es.ulpgc.pamn.pector.data.User
import es.ulpgc.pamn.pector.data.UserRepository
import es.ulpgc.pamn.pector.data.Result

class SignupViewModel : ViewModel() {
    private val userRepository: UserRepository = FirebaseUserRepository()
    private val _signupState = MutableLiveData<Result>()
    val signupState: LiveData<Result> = _signupState;
    fun onSignup(username: String, email: String, password: String) {
        var user = User(name = username, email = email, password = password);
        userRepository.createUser(user){ result: Result ->
            _signupState.value = result
        }
    }

    fun clearError(){
        _signupState.value = null
    }
}