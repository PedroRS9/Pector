package es.ulpgc.pamn.pector.screens.mainmenu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import es.ulpgc.pamn.pector.data.FirebaseImageRepository
import es.ulpgc.pamn.pector.data.ImageRepository
import es.ulpgc.pamn.pector.data.Result
import es.ulpgc.pamn.pector.data.User

class MainMenuViewModel : ViewModel() {
    private val imageRepository: ImageRepository = FirebaseImageRepository()
    private val _imageState = MutableLiveData<Result>()
    val imageState: LiveData<Result> = _imageState
    fun onLoad(user: User){
        user.getPictureURL()?.let {
            imageRepository.downloadImage(it){ result: Result ->
                _imageState.value = result
            }
        }
    }
}