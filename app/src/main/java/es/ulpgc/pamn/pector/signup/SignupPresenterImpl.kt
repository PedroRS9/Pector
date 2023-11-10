package es.ulpgc.pamn.pector.signup

import es.ulpgc.pamn.pector.data.FirebaseUserRepository
import es.ulpgc.pamn.pector.data.User
import es.ulpgc.pamn.pector.data.UserRepository
import es.ulpgc.pamn.pector.data.Result

class SignupPresenterImpl(private val view: SignupView): SignupPresenter {
    private val userRepository: UserRepository = FirebaseUserRepository()
    override fun onSignup(username: String, email: String, password: String) {
        var user = User(username, email, password);
        userRepository.createUser(user){ result: Result ->
            when(result){
                is Result.Success -> {
                    view.navigateToHome()
                }
                is Result.Error -> {
                    view.showErrorMessage(result.exception?.message ?: "Error desconocido")
                }
            }
        }
    }

}