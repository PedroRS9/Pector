package es.ulpgc.pamn.pector.screens.pasapalabra

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import es.ulpgc.pamn.pector.data.User
import es.ulpgc.pamn.pector.extensions.pectorBackground
import es.ulpgc.pamn.pector.global.UserGlobalConf
import es.ulpgc.pamn.pector.navigation.AppScreens
import es.ulpgc.pamn.pector.ui.theme.PectorTheme

@Composable
fun PasapalabraEndScreen(navController: NavController, backStackEntry: NavBackStackEntry, userGlobalConf: UserGlobalConf){
    val correctAnswers = backStackEntry.arguments?.getString("correctAnswers")?.toInt() ?: 0
    val incorrectAnswers = backStackEntry.arguments?.getString("incorrectAnswers")?.toInt() ?: 0
    val unanswered = backStackEntry.arguments?.getString("unanswered")?.toInt() ?: 0
    val points = backStackEntry.arguments?.getString("points")?.toInt() ?: 0

    BodyContent(
        navController = navController,
        user = userGlobalConf.currentUser.value!!,
        correctAnswers = correctAnswers,
        incorrectAnswers = incorrectAnswers,
        unanswered = unanswered,
        points = points
    )
}



@Composable
fun BodyContent(
    navController: NavController,
    user: User,
    correctAnswers: Int,
    incorrectAnswers: Int,
    unanswered: Int,
    points: Int
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .pectorBackground(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Text(
            text = "Resultado del Juego",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "Respuestas Correctas: $correctAnswers")
        Text(text = "Respuestas Incorrectas: $incorrectAnswers")
        Text(text = "Respuestas Sin Responder: $unanswered")
        Text(text = "Puntos Totales: $points")
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = { navController.navigate(route = AppScreens.MainMenuScreen.route) },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(text = "Regresar al Menú", color = Color.White)
        }
    }
}

@Preview
@Composable
fun ShowPreviewEnd(){
    PectorTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            BodyContent(
                navController = rememberNavController(),
                user = User("PedroRS9", "", "", null, 1, 50),
                correctAnswers = 5,  // Valor ficticio para la vista previa
                incorrectAnswers = 2, // Valor ficticio para la vista previa
                unanswered = 3,       // Valor ficticio para la vista previa
                points = 25           // Valor ficticio para la vista previa
            )
        }
    }
}
