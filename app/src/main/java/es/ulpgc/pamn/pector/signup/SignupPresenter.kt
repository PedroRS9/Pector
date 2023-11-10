package es.ulpgc.pamn.pector.signup

interface SignupPresenter {
    fun onSignup(username: String, email: String, password: String)
}