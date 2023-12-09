package es.ulpgc.pamn.pector.screens.pasapalabra

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import es.ulpgc.pamn.pector.data.User
import es.ulpgc.pamn.pector.extensions.pectorBackground
import es.ulpgc.pamn.pector.global.UserGlobalConf
import es.ulpgc.pamn.pector.ui.theme.PectorTheme

@Composable
fun PasapalabraScreen(navController: NavController, backStackEntry: NavBackStackEntry, userGlobalConf: UserGlobalConf) {
    val viewModel = PasapalabraViewModel()
    BodyContent(
        navController = navController,
        user = userGlobalConf.currentUser.value!!,
        viewModel = viewModel
    )
}

@Composable
fun BodyContent(
    navController: NavController,
    user: User,
    viewModel: PasapalabraViewModel
) {
    val roscoItems = viewModel.roscoWords.value ?: emptyList()
    val currentIndex = viewModel.currentIndex.value
    val currentWordItem = roscoItems.getOrNull(currentIndex)
    val wordStates = viewModel.wordStates.value

    Column(
        modifier = Modifier.pectorBackground(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Mostrar letras del rosco
        roscoItems.forEachIndexed { index, wordItem ->
            val state = wordStates[wordItem.word] ?: WordAnswerState.Unanswered
            PasapalabraItem(
                letter = wordItem.word.first(),
                state = state,
                isCurrent = index == currentIndex
            )
        }

        // Mostrar la descripción de la palabra actual como pista
        Text("Pista: ${currentWordItem?.description ?: ""}")

        // Cuadro de texto para la respuesta
        OutlinedTextField(
            value = viewModel.currentAnswer.value,
            onValueChange = { viewModel.onAnswerChanged(it) },
            label = { Text("Respuesta") }
        )

        // Botón para enviar respuesta
        Button(onClick = {
            viewModel.onSubmitAnswer()
        }) {
            Text("Enviar respuesta")
        }
    }
}

@Composable
fun PasapalabraItem(letter: Char, state: WordAnswerState, isCurrent: Boolean) {
    val backgroundColor = when {
        isCurrent -> Color.Blue
        state == WordAnswerState.Correct -> Color.Green
        state == WordAnswerState.Incorrect -> Color.Red
        else -> Color.Gray
    }

    Box(
        modifier = Modifier
            .size(50.dp)
            .background(backgroundColor)
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = letter.toString(),
            color = Color.White,
            fontSize = 20.sp
        )
    }
}

@Preview
@Composable
fun ShowPreviewPasapalabra() {
    PectorTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            BodyContent(
                navController = rememberNavController(),
                user = User("PedroRS9", "", "", null, 1, 50),
                viewModel = PasapalabraViewModel()
            )
        }
    }
}
