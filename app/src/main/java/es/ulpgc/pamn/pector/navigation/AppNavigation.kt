package es.ulpgc.pamn.pector.navigation

import TestScreen
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import es.ulpgc.pamn.pector.global.UserGlobalConf
import es.ulpgc.pamn.pector.screens.login.LoginScreen
import es.ulpgc.pamn.pector.screens.mainmenu.MainMenuScreen
import es.ulpgc.pamn.pector.screens.pasapalabra.PasapalabraEndScreen
import es.ulpgc.pamn.pector.screens.profile.ProfileScreen
import es.ulpgc.pamn.pector.screens.signup.SignupScreen
import es.ulpgc.pamn.pector.screens.test.TestLeaderboardScreen
import es.ulpgc.pamn.pector.screens.pasapalabra.PasapalabraLeaderboardScreen
import es.ulpgc.pamn.pector.screens.pasapalabra.PasapalabraScreen
import es.ulpgc.pamn.pector.screens.welcomemenu.WelcomeScreen
@Composable
fun AppNavigation(onVoiceButtonClicked: () -> Unit, speakFunction: (String) -> Unit) {

    val navController = rememberNavController()
    val userGlobalConf = UserGlobalConf()

    NavHost(navController = navController, startDestination = AppScreens.WelcomeScreen.route){
        composable(route = AppScreens.WelcomeScreen.route){
            WelcomeScreen(navController)
        }
        composable(route = AppScreens.LoginScreen.route){ backStackEntry ->
            LoginScreen(navController, backStackEntry, userGlobalConf)
        }
        composable(route = AppScreens.SignupScreen.route){ backStackEntry ->
            SignupScreen(navController, backStackEntry)
        }
        composable(route = AppScreens.MainMenuScreen.route){ backStackEntry ->
            MainMenuScreen(navController, backStackEntry, userGlobalConf)
        }
        composable(route = AppScreens.ProfileScreen.route){ backStackEntry ->
            ProfileScreen(navController, backStackEntry, userGlobalConf)
        }

        composable(route = AppScreens.TestLeaderboardScreen.route){ backStackEntry ->
            TestLeaderboardScreen(navController, backStackEntry, userGlobalConf)
        }
        composable(route = AppScreens.TestScreen.route){ backStackEntry ->
            TestScreen(navController, backStackEntry, userGlobalConf)
        }
        composable(route = AppScreens.PasapalabraLeaderboardScreen.route){ backStackEntry ->
            PasapalabraLeaderboardScreen(navController, backStackEntry, userGlobalConf)
        }
        composable(route = AppScreens.PasapalabraScreen.route){ backStackEntry ->
            PasapalabraScreen(navController, backStackEntry, userGlobalConf, onVoiceButtonClicked, speakFunction)
        }
        composable(route = AppScreens.PasapalabraEndScreen.route){ backStackEntry ->
            PasapalabraEndScreen(navController, backStackEntry, userGlobalConf)
        }
    }
}
