import android.media.MediaPlayer
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import es.ulpgc.pamn.pector.R
import es.ulpgc.pamn.pector.components.AnimatedExperienceBar
import es.ulpgc.pamn.pector.components.ExperienceBar
import es.ulpgc.pamn.pector.data.Question
import es.ulpgc.pamn.pector.data.Result
import es.ulpgc.pamn.pector.data.User
import es.ulpgc.pamn.pector.extensions.pectorBackground
import es.ulpgc.pamn.pector.global.UserGlobalConf
import es.ulpgc.pamn.pector.navigation.AppScreens
import es.ulpgc.pamn.pector.screens.test.TestGameState
import es.ulpgc.pamn.pector.screens.test.TestViewModel
import es.ulpgc.pamn.pector.ui.theme.CornflowerBlue
import es.ulpgc.pamn.pector.ui.theme.DarkViolet
import es.ulpgc.pamn.pector.ui.theme.LightGrey
import es.ulpgc.pamn.pector.ui.theme.OptionSelectedColor
import es.ulpgc.pamn.pector.ui.theme.PectorTheme
import kotlinx.coroutines.delay

@Composable
fun TestScreen(navController: NavController, backStackEntry: NavBackStackEntry, userGlobalConf: UserGlobalConf){
    val viewModel: TestViewModel = viewModel(backStackEntry)
    val testGameState by viewModel.gameState.observeAsState()
    BodyContent(
        navController = navController,
        user = userGlobalConf.currentUser.value!!,
        downloadQuestions = { viewModel.getQuestions(10, "test") },
        onOptionClick = { viewModel.onOptionSelected(it) },
        testGameState = testGameState,
        mMediaPlayer = MediaPlayer.create(LocalContext.current, R.raw.optionselected)
    )
}

@Composable
fun BodyContent(
    navController: NavController,
    user: User,
    downloadQuestions: () -> Unit,
    onOptionClick: (String) -> Unit,
    testGameState: TestGameState?,
    mMediaPlayer: MediaPlayer?
){
    LaunchedEffect(Unit) {
        downloadQuestions()
    }
    Column(
        modifier = Modifier.pectorBackground(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        when(testGameState){
            is TestGameState.Loading -> {
                Text(text = "LOADING...")
            }
            is TestGameState.AnsweringQuestion -> {
                val question = testGameState.question
                ShowEnunciadoAndOptions(question = question, mediaPlayer = mMediaPlayer, onOptionClick = onOptionClick)
            }
            is TestGameState.CheckingIfOptionIsCorrect -> {
                val question = testGameState.question
                val option = testGameState.option
                ShowEnunciadoAndOptions(question = question, mediaPlayer = mMediaPlayer, selectedOption = option, onOptionClick = {})
            }
            is TestGameState.ShowCorrectOption -> {
                val question = testGameState.question
                val selectedOption = testGameState.selectedOption
                ShowEnunciadoAndOptions(question = question,
                    mediaPlayer = mMediaPlayer,
                    selectedOption = selectedOption,
                    correctOption = testGameState.correctOption,
                    onOptionClick = {})
            }
            is TestGameState.EndGame -> {
                val correctAnswers = testGameState.correctAnswers
                val totalQuestions = testGameState.totalQuestions
                val earnedPoints = testGameState.earnedPoints
                Column(
                    // centramos el contenido
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text(text = "RESULTADOS", color = Color.White, fontSize = 40.sp, modifier = Modifier.padding(20.dp))
                    Spacer(modifier = Modifier.size(30.dp))
                    // PUNTOS GANADOS
                    Text(text = "PREGUNTAS CORRECTAS: $correctAnswers de $totalQuestions", color = Color.White, fontSize = 20.sp)
                    Text(text = "PUNTOS GANADOS: $earnedPoints", color = Color.White, fontSize = 20.sp)
                    Text(text = "PUNTOS DE EXPERIENCIA", color = Color.White, fontSize = 20.sp)
                    AnimatedExperienceBar(50, 90)
                    Spacer(modifier = Modifier.size(60.dp))
                    Text(text = "¿Quieres jugar de nuevo?", color = Color.White, fontSize = 20.sp)
                    Button(
                        onClick = { navController.navigate(AppScreens.TestScreen.route) },
                        colors = ButtonDefaults.buttonColors(CornflowerBlue),
                        modifier = Modifier
                            .padding(10.dp)
                            .size(130.dp, 50.dp)
                    ) {
                        Text(
                            text = "JUGAR",
                            color = Color.White,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                    }
                    // volver al menú principal
                    Button(
                        onClick = { navController.navigate(AppScreens.MainMenuScreen.route) },
                        colors = ButtonDefaults.buttonColors(CornflowerBlue),
                        modifier = Modifier
                            .padding(10.dp)
                            .size(300.dp, 50.dp)
                    ) {
                        Text(
                            text = "Volver al menú principal",
                            color = Color.White,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
                

            }
            else -> {
                Text(text = "ERROR")
            }
        }
    }
}

@Composable
fun ShowEnunciadoAndOptions(
    question: Question,
    mediaPlayer: MediaPlayer?,
    selectedOption: String? = null,
    correctOption: String? = null,
    onOptionClick: (String) -> Unit
){
    Spacer(modifier = Modifier.size(80.dp))
    QuestionEnunciado(enunciado = question.question)
    Spacer(modifier = Modifier.size(20.dp))
    question.options.forEach { option ->
        QuestionOption(option = option,
            isSelected = (option == selectedOption),
            // isCorrect será null si correctOption es null, y si no, será true si option == correctOption y false si no
            isCorrect = correctOption?.let { option == it },
            onOptionClick = {
                mediaPlayer?.start()
                onOptionClick(option)
        })
    }
}

@Composable
fun QuestionEnunciado(enunciado: String){
    Box(
        modifier = Modifier
            .size(330.dp, 200.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(DarkViolet),
        contentAlignment = Alignment.TopCenter
    ) {
        Text(
            text = enunciado,
            color = Color.White,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(3.dp)
        )
    }
}
@Composable
fun QuestionOption(option: String, isSelected: Boolean = false, isCorrect: Boolean? = null, onOptionClick: (String) -> Unit){
    Button(
        onClick = {
            onOptionClick(option)
        },
        // imprimimos por consola la opción, el valor de isSelected y el valor de isCorrect
        // para comprobar que se actualizan correctamente

        colors = ButtonDefaults.buttonColors(
            when {
                isSelected && isCorrect == null -> OptionSelectedColor
                !isSelected -> LightGrey
                isSelected && isCorrect == true -> Color.Green
                isSelected && isCorrect == false -> Color.Red
                else -> Color.Transparent
            }
        ),

        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .padding(10.dp)
            .size(330.dp, 50.dp)
    ) {
        Text(
            text = option,
            color = Color.Black,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )
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
            val question = Question("PAMN",
                question = "¿Quién es el profesor de PAMN? Y ahora estoy metiendo relleno para que la preview sea más realista",
                correctOption = "Iván Hernández",
                incorrectOptions = arrayOf("Octavio Mayor", "Nelson Monzón", "José Juan Hernández")
            )
            BodyContent(
                navController = rememberNavController(),
                user = User("PedroRS9", "", "", null, 1, 50),
                downloadQuestions = {},
                onOptionClick = {},
                testGameState = TestGameState.EndGame(5, 10, 35),
                mMediaPlayer = null
            )
        }
    }
}