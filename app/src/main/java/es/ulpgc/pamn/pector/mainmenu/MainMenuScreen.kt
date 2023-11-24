package es.ulpgc.pamn.pector.mainmenu

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import es.ulpgc.pamn.pector.extensions.pectorBackground
import es.ulpgc.pamn.pector.ui.theme.PectorTheme

@Composable
fun MainMenuScreen(navController: NavController, backStackEntry: NavBackStackEntry){
    Column{
        BodyContent(
            navController = navController
        )
    }
}

@Composable
fun BodyContent(navController: NavController){
    Column(
        modifier = Modifier.pectorBackground()
    ){
        Text("test")
    }
}

@Preview
@Composable
fun ShowPreview(){
    PectorTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            BodyContent(
                navController = rememberNavController()
            )
        }
    }
}