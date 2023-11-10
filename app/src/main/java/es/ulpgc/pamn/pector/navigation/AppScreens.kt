package es.ulpgc.pamn.pector.navigation

sealed class AppScreens(val route: String){
    object SignupScreen: AppScreens("signup_screen")
}
