package es.ulpgc.pamn.pector.global

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class SharedRepository {
    private val _recognizedText = MutableLiveData<String>()

    // Método para actualizar el texto
    fun updateRecognizedText(text: String) {
        _recognizedText.postValue(text)
    }

    // Método para observar el texto
    fun getRecognizedText(): LiveData<String> = _recognizedText
}
object SharedRepositoryInstance {
    val repository = SharedRepository()
}

