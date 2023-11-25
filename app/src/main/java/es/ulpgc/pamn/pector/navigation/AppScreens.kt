package es.ulpgc.pamn.pector.navigation

sealed class AppScreens(val route: String){
    object LoginScreen: AppScreens("login_screen")
    object SignupScreen: AppScreens("signup_screen")
    object MainMenuScreen: AppScreens("mainmenu_screen")
    object WelcomeScreen : AppScreens("welcome_screen")
}
