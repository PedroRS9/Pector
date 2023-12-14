package es.ulpgc.pamn.pector.screens.mainmenu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import es.ulpgc.pamn.pector.data.FirebaseImageRepository
import es.ulpgc.pamn.pector.data.ImageRepository
import es.ulpgc.pamn.pector.data.Result
import es.ulpgc.pamn.pector.data.User
import es.ulpgc.pamn.pector.global.UserGlobalConf

class MainMenuViewModel(private val userGlobalConf: UserGlobalConf) : ViewModel() {
    private val imageRepository: ImageRepository = FirebaseImageRepository()
    private val _imageState = MutableLiveData<Result>()
    val imageState: LiveData<Result> = _imageState
    fun checkIfPictureIsDownloaded(){
        val user = userGlobalConf.currentUser.value!!
        val userHasProfilePicture = user.getPictureURL() != null
        val pictureIsDownloaded = user.getPicture() != null
        if(userHasProfilePicture){
            if(pictureIsDownloaded){
                _imageState.value = Result.ImageSuccess(user.getPicture()!!)
            } else {
                downloadPicture(user.getPictureURL()!!)
            }
        }
    }

    fun downloadPicture(pictureURL: String){
        imageRepository.downloadImage(pictureURL){ result: Result ->
            if(result is Result.ImageSuccess){
                userGlobalConf.currentUser.value!!.setPicture(result.bytes)
            }
            _imageState.value = result
        }
    }
}