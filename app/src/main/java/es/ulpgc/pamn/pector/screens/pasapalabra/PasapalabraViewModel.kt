package es.ulpgc.pamn.pector.screens.pasapalabra

import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import androidx.compose.runtime.mutableStateOf
import java.text.Normalizer

class PasapalabraViewModel : ViewModel() {

    private val wordItems = listOf(
        WordItem("ALMACÉN", "Lugar donde se guardan mercancías"),
        WordItem("BAILE", "Actividad donde se mueven al ritmo de la música"),
        WordItem("COCINA", "Lugar donde se preparan los alimentos"),
        WordItem("DANZA", "Forma de expresión artística a través del movimiento"),
        WordItem("ESCALERA", "Estructura para subir o bajar entre niveles"),
        WordItem("FLORES", "Parte reproductora de las plantas"),
        WordItem("GUITARRA", "Instrumento musical de cuerdas"),
        WordItem("HOSPITAL", "Institución de atención médica"),
        WordItem("ILUSIÓN", "Percepción errónea de la realidad"),
        WordItem("JARDÍN", "Área con plantas y flores ornamentales"),
        WordItem("KIOSKO", "Pequeña construcción para vender productos"),
        WordItem("LIBRO", "Objeto con páginas y texto para leer"),
        WordItem("MONTAÑA", "Gran elevación de terreno"),
        WordItem("NATURALEZA", "Conjunto de elementos del entorno natural"),
        WordItem("OCEANO", "Gran extensión de agua salada"),
        WordItem("PAISAJE", "Vista panorámica de la naturaleza"),
        WordItem("QUESO", "Producto lácteo sólido"),
        WordItem("RELOJ", "Dispositivo para medir el tiempo"),
        WordItem("SOL", "Estrella que ilumina la Tierra"),
        WordItem("TREN", "Medio de transporte sobre rieles"),
        WordItem("UNIVERSO", "Todo lo que existe en el espacio"),
        WordItem("VOLCÁN", "Montaña que expulsa lava y ceniza"),
        WordItem("WIFI", "Conexión inalámbrica a internet"),
        WordItem("XILÓFONO", "Instrumento musical de percusión"),
        WordItem("YOGA", "Práctica física y espiritual"),
        WordItem("ZOOLOGÍA", "Estudio de los animales y sus características")
    )


    val currentAnswer = mutableStateOf("")
    val roscoWords = MutableLiveData<List<WordItem>>(wordItems)
    val currentIndex = mutableStateOf(0)
    val wordStates = mutableStateOf<Map<String, WordAnswerState>>(emptyMap())

    init {
        initializeWordStates()
    }

    private fun initializeWordStates() {
        val initialStates = roscoWords.value!!.associate { it.word to WordAnswerState.Unanswered }
        wordStates.value = initialStates
    }

    fun onAnswerChanged(answer: String) {
        currentAnswer.value = answer
    }

    fun onSubmitAnswer() {
        val currentWord = normalizeString(roscoWords.value!![currentIndex.value].word)
        val userAnswer = normalizeString(currentAnswer.value)

        val isCorrect = userAnswer.equals(currentWord, ignoreCase = true)
        val newState = if (isCorrect) WordAnswerState.Correct else WordAnswerState.Incorrect

        wordStates.value = wordStates.value.toMutableMap().apply { put(roscoWords.value!![currentIndex.value].word, newState) }

        moveToNextWord()
    }
    fun onPasapalabra() {
        moveToNextWord()
    }

    private fun moveToNextWord() {
        var nextIndex = (currentIndex.value + 1) % roscoWords.value!!.size
        // Bucle para encontrar la siguiente palabra no respondida y no pasada
        while (wordStates.value[roscoWords.value!![nextIndex].word] != WordAnswerState.Unanswered) {
            nextIndex = (nextIndex + 1) % roscoWords.value!!.size
        }
        currentIndex.value = nextIndex
        currentAnswer.value = ""
    }
    private fun normalizeString(input: String): String {
        val normalized = Normalizer.normalize(input, Normalizer.Form.NFD)
        val accentsRemoved = normalized.replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")
        return accentsRemoved.trim()
    }
}

enum class WordAnswerState {
    Correct, Incorrect, Unanswered
}



