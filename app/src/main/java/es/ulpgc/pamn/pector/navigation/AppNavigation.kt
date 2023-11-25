package es.ulpgc.pamn.pector.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHost
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import es.ulpgc.pamn.pector.global.UserViewModel
import es.ulpgc.pamn.pector.login.LoginScreen
import es.ulpgc.pamn.pector.mainmenu.MainMenuScreen
import es.ulpgc.pamn.pector.signup.SignupScreen
import es.ulpgc.pamn.pector.welcomemenu.WelcomeScreen
@Composable
fun AppNavigation() {

    val navController = rememberNavController()
    val userViewModel: UserViewModel = viewModel()

    NavHost(navController = navController, startDestination = AppScreens.WelcomeScreen.route){
        composable(route = AppScreens.LoginScreen.route){ backStackEntry ->
            LoginScreen(navController, backStackEntry, userViewModel)
        }
        composable(route = AppScreens.SignupScreen.route){ backStackEntry ->
            SignupScreen(navController, backStackEntry)
        }
        composable(route = AppScreens.MainMenuScreen.route){ backStackEntry ->
            MainMenuScreen(navController, backStackEntry, userViewModel)
        }
        composable(route = AppScreens.WelcomeScreen.route){
            WelcomeScreen(navController)
        }
    }
}
