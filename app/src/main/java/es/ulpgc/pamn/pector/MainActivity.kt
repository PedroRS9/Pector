package es.ulpgc.pamn.pector

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import es.ulpgc.pamn.pector.navigation.AppNavigation
import es.ulpgc.pamn.pector.screens.pasapalabra.PasapalabraViewModel
import es.ulpgc.pamn.pector.ui.theme.PectorTheme
import java.util.Locale
import es.ulpgc.pamn.pector.global.SharedRepository
import es.ulpgc.pamn.pector.global.SharedRepositoryInstance

class MainActivity : ComponentActivity() {
    private lateinit var startVoiceRecognitionActivity: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startVoiceRecognitionActivity =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                    val matches = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    if (!matches.isNullOrEmpty()) {
                        SharedRepositoryInstance.repository.updateRecognizedText(matches[0])
                    }
                }
            }

        setContent {
            PectorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation { startVoiceRecognition() }
                }
            }
        }
    }

    private fun startVoiceRecognition() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es-ES") // Configuración para español de España
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Habla ahora")
        }
        startVoiceRecognitionActivity.launch(intent)
    }

}