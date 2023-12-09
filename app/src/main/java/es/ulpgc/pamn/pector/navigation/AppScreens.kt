package es.ulpgc.pamn.pector.navigation

sealed class AppScreens(val route: String){
    object WelcomeScreen : AppScreens("welcome_screen")
    object LoginScreen: AppScreens("login_screen")
    object SignupScreen: AppScreens("signup_screen")
    object MainMenuScreen: AppScreens("mainmenu_screen")
    object ProfileScreen: AppScreens("profile_screen")
    object TestLeaderboardScreen : AppScreens("test_leaderboard_screen")
    object TestScreen : AppScreens("test_screen")
    object PasapalabraLeaderboardScreen : AppScreens("pasapalabra_leaderboard_screen")
    object PasapalabraScreen : AppScreens("pasapalabra_screen")
}
