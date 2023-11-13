package es.ulpgc.pamn.pector.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHost
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import es.ulpgc.pamn.pector.signup.SignupScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = AppScreens.SignupScreen.route){
        composable(route = AppScreens.SignupScreen.route){ backStackEntry ->
            SignupScreen(navController, backStackEntry)
        }
    }
}
