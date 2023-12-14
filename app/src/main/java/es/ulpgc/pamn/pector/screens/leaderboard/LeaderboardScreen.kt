package es.ulpgc.pamn.pector.screens.leaderboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import es.ulpgc.pamn.pector.R
import es.ulpgc.pamn.pector.data.User
import es.ulpgc.pamn.pector.data.Result
import es.ulpgc.pamn.pector.data.TopScore
import es.ulpgc.pamn.pector.global.UserGlobalConf
import es.ulpgc.pamn.pector.components.PectorButton
import es.ulpgc.pamn.pector.components.PectorLeaderboard
import es.ulpgc.pamn.pector.extensions.pectorBackground
import es.ulpgc.pamn.pector.navigation.AppScreens
import es.ulpgc.pamn.pector.navigation.BottomNavigationBar
import es.ulpgc.pamn.pector.screens.pasapalabra.BodyContent
import es.ulpgc.pamn.pector.ui.theme.PectorTheme

@Composable
fun LeaderboardScreen(
    navController: NavController,
    backStackEntry: NavBackStackEntry,
    userGlobalConf: UserGlobalConf,
    gameType: String
){
    val viewModel: LeaderboardViewModel = viewModel(backStackEntry)
    val leaderboardState by viewModel.leaderboardState.observeAsState()
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { paddingValues ->
        paddingValues /* TODO: Remove this line. paddingValues should be passed as a parameter */
        LeaderboardContent(
            navController = navController,
            leaderboardState = leaderboardState,
            downloadLeaderboard = { viewModel.getLeaderboard(gameType) },
            gameType = gameType,
            paddingValues = paddingValues
        )
    }
}

@Composable
fun LeaderboardContent(
    navController: NavController,
    leaderboardState: Result?,
    downloadLeaderboard: () -> Unit,
    gameType: String,
    paddingValues: PaddingValues = PaddingValues(0.dp)
){
    Column(
        modifier = Modifier
            .pectorBackground()
            .padding(start = 5.dp, end = 5.dp, top = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val leaderboard = when (leaderboardState) {
            is Result.LeaderboardSuccess -> leaderboardState.leaderboard
            else -> ArrayList()
        }

        LaunchedEffect(Unit) {
            downloadLeaderboard()
        }

        val gameImage = when (gameType) {
            "pasapalabra" -> R.drawable.pasapalabra // Reemplaza con el recurso de imagen correcto
            "test" -> R.drawable.test_image // Reemplaza con el recurso de imagen correcto
            else -> R.drawable.game5 // Imagen predeterminada para otros juegos
        }

        Image(
            painter = painterResource(id = gameImage),
            contentDescription = null,
            modifier = Modifier
                .size(width = 250.dp, height = 200.dp)
                .clip(RoundedCornerShape(12.dp)) // Aplicar esquinas redondeadas
                .padding(8.dp)
        )


        PectorButton(
            text = "JUGAR",
            onClick = {
                navController.navigate(
                    when (gameType) {
                        "pasapalabra" -> AppScreens.PasapalabraScreen.route
                        "test" -> AppScreens.TestScreen.route
                        else -> AppScreens.MainMenuScreen.route
                    }
                )
            },
            modifier = Modifier.fillMaxSize()
        )

        Spacer(modifier = Modifier.size(10.dp))
        PectorLeaderboard(leaderboard)
    }
}

@Preview
@Composable
fun LeaderboardPreview(){
    PectorTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            // we create an arraylist of top scores with fake data
            val topScores = ArrayList<TopScore>()
            for (i in 1..5){
                topScores.add(TopScore(username = "User$i", score = 500-(100*(i-1)), date = "01/01/2021", gameType = "test"))
            }

            LeaderboardContent(
                navController = rememberNavController(),
                leaderboardState = Result.LeaderboardSuccess(topScores),
                downloadLeaderboard =  {},
                gameType = "test"
            )
        }
    }
}