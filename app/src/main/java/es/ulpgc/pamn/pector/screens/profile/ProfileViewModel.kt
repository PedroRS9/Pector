package es.ulpgc.pamn.pector.screens.profile

import android.content.Intent
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import es.ulpgc.pamn.pector.data.FirebaseImageRepository
import es.ulpgc.pamn.pector.data.FirebaseUserRepository
import es.ulpgc.pamn.pector.data.ImageRepository
import es.ulpgc.pamn.pector.data.Result
import es.ulpgc.pamn.pector.data.User
import es.ulpgc.pamn.pector.data.UserRepository

class ProfileViewModel : ViewModel() {

    private val imageRepository: ImageRepository = FirebaseImageRepository()
    private val _imageState = MutableLiveData<Result>()
    val imageState: LiveData<Result> = _imageState;

    private val userRepository: UserRepository = FirebaseUserRepository()
    private val _updateState = MutableLiveData<Result>()
    val updateState: LiveData<Result> = _updateState;
    fun onLoad(user: User){
        user.getPictureURL()?.let {
            imageRepository.downloadImage(it){ result: Result ->
                _imageState.value = result
            }
        }
    }

    fun onChooseImage(filename: String, byteArray: ByteArray){
        imageRepository.uploadImage(filename, byteArray){ result: Result ->
            _imageState.value = result
        }
    }

    fun onImageUploaded(user: User){
        userRepository.updateUser(user){ result: Result ->
            _updateState.value = result
        }
    }

    fun clearError() {
        _imageState.value = null
    }
}