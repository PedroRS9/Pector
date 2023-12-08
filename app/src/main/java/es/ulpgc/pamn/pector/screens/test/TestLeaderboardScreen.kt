package es.ulpgc.pamn.pector.screens.test

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import es.ulpgc.pamn.pector.components.PectorButton
import es.ulpgc.pamn.pector.components.PectorLeaderboard
import es.ulpgc.pamn.pector.data.Result
import es.ulpgc.pamn.pector.data.TopScore
import es.ulpgc.pamn.pector.data.User
import es.ulpgc.pamn.pector.extensions.pectorBackground
import es.ulpgc.pamn.pector.global.UserGlobalConf
import es.ulpgc.pamn.pector.navigation.AppScreens
import es.ulpgc.pamn.pector.ui.theme.PectorTheme

@Composable
fun TestLeaderboardScreen(navController: NavController, backStackEntry: NavBackStackEntry, userGlobalConf: UserGlobalConf){
    val viewModel: TestLeaderboardViewModel = viewModel(backStackEntry)
    val leaderboardState by viewModel.leaderboardState.observeAsState()
    BodyContent(
        navController = navController,
        user = userGlobalConf.currentUser.value!!,
        leaderboardState = leaderboardState,
        downloadLeaderboard = { viewModel.getLeaderboard() }
    )
}

@Composable
fun BodyContent(
    navController: NavController,
    user: User,
    leaderboardState: Result?,
    downloadLeaderboard: () -> Unit
){
    Column(
        modifier = Modifier.pectorBackground(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        val leaderboard = when (leaderboardState) {
            is Result.LeaderboardSuccess -> leaderboardState.leaderboard
            else -> ArrayList<TopScore>() // empty list
        }
        LaunchedEffect(Unit){
            downloadLeaderboard()
        }
        PectorButton(
            text = "JUGAR",
            onClick = { navController.navigate(AppScreens.TestScreen.route) },
            modifier = Modifier.fillMaxSize()
        )
        Spacer(modifier = Modifier.size(20.dp))
        PectorLeaderboard(leaderboard)
    }
}

@Preview
@Composable
fun ShowPreview(){
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

            BodyContent(
                navController = rememberNavController(),
                user = User("PedroRS9", "", "", null, 1, 50),
                leaderboardState = Result.LeaderboardSuccess(topScores),
                downloadLeaderboard =  {}
            )
        }
    }
}