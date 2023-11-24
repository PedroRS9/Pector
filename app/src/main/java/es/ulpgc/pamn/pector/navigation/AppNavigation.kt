package es.ulpgc.pamn.pector.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHost
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import es.ulpgc.pamn.pector.login.LoginScreen
import es.ulpgc.pamn.pector.mainmenu.MainMenuScreen
import es.ulpgc.pamn.pector.signup.SignupScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = AppScreens.LoginScreen.route){
        composable(route = AppScreens.LoginScreen.route){ backStackEntry ->
            LoginScreen(navController, backStackEntry)
        }
        composable(route = AppScreens.SignupScreen.route){ backStackEntry ->
            SignupScreen(navController, backStackEntry)
        }
        composable(route = AppScreens.MainMenuScreen.route){ backStackEntry ->
            MainMenuScreen(navController, backStackEntry)
        }
    }
}
