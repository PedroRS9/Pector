package es.ulpgc.pamn.pector.signup

interface SignupView {
    fun showErrorMessage(error: String)
    fun navigateToHome()
}