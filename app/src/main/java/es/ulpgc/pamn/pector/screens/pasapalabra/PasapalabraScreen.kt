package es.ulpgc.pamn.pector.screens.pasapalabra

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import es.ulpgc.pamn.pector.data.User
import es.ulpgc.pamn.pector.data.WordItem
import es.ulpgc.pamn.pector.extensions.pectorBackground
import es.ulpgc.pamn.pector.global.UserGlobalConf
import es.ulpgc.pamn.pector.ui.theme.PectorTheme
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun PasapalabraScreen(
    navController: NavController,
    backStackEntry: NavBackStackEntry,
    userGlobalConf: UserGlobalConf,
    onVoiceButtonClicked: () -> Unit,
    speakFunction: (String) -> Unit) {
    val viewModel: PasapalabraViewModel = viewModel()
    val isLoading = viewModel.isLoading.observeAsState(initial = true)

    if (isLoading.value) {
        LoadingScreen()
    } else {
        GameContent(navController, userGlobalConf.currentUser.value!!, viewModel, onVoiceButtonClicked)
    }
}
@Composable
fun LoadingScreen() {
    // Puedes personalizar este composable para mostrar un indicador de carga
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}
@Composable
fun GameContent(
    navController: NavController,
    user: User,
    viewModel: PasapalabraViewModel,
    onVoiceButtonClicked: () -> Unit
) {
    BodyContent(navController = navController, user = user, viewModel = viewModel, onVoiceButtonClicked)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BodyContent(
    navController: NavController,
    user: User,
    viewModel: PasapalabraViewModel,
    onVoiceButtonClicked: () -> Unit
) {
    val roscoItems = viewModel.roscoWords.value ?: emptyList()
    val currentIndex = viewModel.currentIndex.value
    val currentWordItem = roscoItems.getOrNull(currentIndex)
    val wordStates = viewModel.wordStates.value

    // Observa el tiempo restante del cronómetro
    val currentTime = viewModel.currentTime.value
    val navigationEvent by viewModel.navigationEvent.observeAsState()
    LaunchedEffect(navigationEvent) {
        when (val event = navigationEvent) {
            is NavigationEvent.Navigate -> {
                navController.navigate(event.route)
            }
            else -> {}
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .pectorBackground()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, end = 16.dp)
        ) {
            // Botón para salir
            IconButton(
                onClick = { viewModel.finalizeGame() },
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Salir",
                    tint = Color.White // Establece el color del icono a blanco
                )
            }

            // Cronómetro
            Text(
                text = "${currentTime}s", // Muestra el tiempo en segundos
                style = TextStyle(
                    color = Color.White,
                    fontSize = 24.sp, // Aumenta el tamaño de la fuente para hacer el cronómetro más grande
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 16.dp)
            )
        }


        // Rosco circular con un peso para que ocupe la mayor parte de la pantalla
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            RoscoCircular(
                roscoItems = roscoItems,
                wordStates = wordStates,
                currentIndex = currentIndex
            )
        }

        // Cuadro para la pista con bordes redondeados y color de fondo
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp), // Añade un poco de padding horizontal
            color = Color(0xFFE1BEE7), // El color de fondo del cuadro de la pista, cambia al color morado que prefieras
            shape = RoundedCornerShape(8.dp) // Redondea las esquinas
        ) {
            Text(
                text = "Con la ${currentWordItem?.word?.first() ?: ""}: ${currentWordItem?.description ?: ""}",
                modifier = Modifier
                    .padding(16.dp), // Añade padding dentro del cuadro para el texto
                style = TextStyle(
                    color = Color.Black, // Cambia el color del texto si es necesario
                    fontSize = 18.sp // Cambia el tamaño del texto si es necesario
                )
            )
        }
        Spacer(modifier = Modifier.height(25.dp))
        // Cuadro de texto para la respuesta y botón de envío
        Row(modifier = Modifier.padding(16.dp)) {
            OutlinedTextField(
                value = viewModel.currentAnswer.value,
                onValueChange = { viewModel.onAnswerChanged(it) },
                label = { Text("Respuesta", color = Color.White) },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.White,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.White,
                )
            )

            IconButton(onClick = { onVoiceButtonClicked() }) {
                Icon(Icons.Filled.Mic, "mic", tint = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(25.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.Center // Centra los botones en la fila
        ) {
            // Botón "Pasapalabra"
            Button(
                onClick = { viewModel.onPasapalabra() },
                // Establece el color del botón para la acción de "pasar"
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC107)),
                modifier = Modifier
                    .padding(4.dp) // Agrega espacio alrededor del botón
                    .defaultMinSize(minWidth = 128.dp) // Establece un ancho mínimo para el botón
            ) {
                Text("Pasapalabra", color = Color.Black)
            }
            Spacer(modifier = Modifier.width(16.dp)) // Espacio entre botones
            // Botón "Enviar respuesta"
            Button(
                onClick = { viewModel.onSubmitAnswer() },
                // Establece el color del botón para la acción de "enviar"
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                modifier = Modifier
                    .padding(4.dp) // Agrega espacio alrededor del botón
                    .defaultMinSize(minWidth = 128.dp) // Establece un ancho mínimo para el botón
            ) {
                Text("Enviar respuesta", color = Color.White)
            }

        }
        Spacer(modifier = Modifier.height(25.dp))
    }
}



@Composable
fun RoscoCircular(
    roscoItems: List<WordItem>,
    wordStates: Map<String, WordAnswerState>,
    currentIndex: Int,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(modifier = modifier) {
        // Utiliza LocalDensity para convertir unidades Dp a Px
        val density = LocalDensity.current
        val circlePadding = 20.dp
        val circleRadius = 20.dp

        with(density) {
            val radiusPx = (minOf(maxWidth, maxHeight).toPx() / 2f) - circlePadding.toPx()
            val center = Offset(maxWidth.toPx() / 2f, maxHeight.toPx() / 2f)

            val textPaint = Paint().apply {
                color = android.graphics.Color.WHITE
                textAlign = Paint.Align.CENTER
                textSize = 40f // Ajusta el tamaño del texto según tus necesidades
            }

            Canvas(modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center)) {

                roscoItems.forEachIndexed { index, wordItem ->
                    val angle = 2 * PI.toFloat() / roscoItems.size * index - PI.toFloat() / 2
                    val x = center.x + cos(angle) * radiusPx
                    val y = center.y + sin(angle) * radiusPx
                    val state = wordStates[wordItem.word] ?: WordAnswerState.Unanswered

                    // Decide el color y tamaño basado en si es la palabra actual o no
                    val (color, radius) = if (index == currentIndex) {
                        // Resalta la palabra actual
                        Color(0xFF008ADF) to 19.dp.toPx() // Color amarillo y radio mayor
                    } else {
                        // Colores normales para las otras palabras
                        when (state) {
                            WordAnswerState.Correct -> Color.Green
                            WordAnswerState.Incorrect -> Color.Red
                            else -> Color(0xFF0000CD)
                        } to 15.dp.toPx()
                    }

                    drawCircle(
                        color = color,
                        center = Offset(x, y),
                        radius = radius // Usa el radio basado en si es la palabra actual o no
                    )

                    // Dibuja el texto de la letra aquí
                    drawContext.canvas.nativeCanvas.drawText(
                        wordItem.word.first().toString(),
                        x,
                        y - (textPaint.ascent() + textPaint.descent()) / 2,
                        textPaint
                    )
                }
            }
        }
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
                viewModel = PasapalabraViewModel(),
                onVoiceButtonClicked = {}
            )
        }
    }
}
