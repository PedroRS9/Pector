package es.ulpgc.pamn.pector.login

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import es.ulpgc.pamn.pector.components.PectorButton
import es.ulpgc.pamn.pector.navigation.AppScreens

@Composable
fun LoginScreen(navController: NavController, backStackEntry: NavBackStackEntry){
    Column{
        BodyContent(
            navController = navController
        )
    }
}

@Composable
fun BodyContent(navController: NavController
                // ...
) {
    PectorButton(
        onClick = { navController.navigate(route = AppScreens.SignupScreen.route) },
        text = "Registrarse",
    )
}