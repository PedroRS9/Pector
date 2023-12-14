package es.ulpgc.pamn.pector.screens.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import es.ulpgc.pamn.pector.data.FirebaseImageRepository
import es.ulpgc.pamn.pector.data.FirebaseUserRepository
import es.ulpgc.pamn.pector.data.ImageRepository
import es.ulpgc.pamn.pector.data.SearchResult
import es.ulpgc.pamn.pector.data.UserRepository
import es.ulpgc.pamn.pector.data.Result

class SearchViewModel : ViewModel(){

    val userRepository: UserRepository = FirebaseUserRepository()
    val imageRepository: ImageRepository = FirebaseImageRepository()
    private val _searchState = MutableLiveData<SearchResult>()
    val searchState: LiveData<SearchResult> = _searchState

    fun onSearch(query: String) {
        _searchState.value = SearchResult.Loading
        userRepository.searchUsers(query){ result ->
            _searchState.value = result
            if(result is SearchResult.ShowResults){
                // for each user in the list, we download the profile picture
                val updatedResults = result.results
                updatedResults.forEach { user ->
                    if(user.getPictureURL() != null){
                        imageRepository.downloadImage(user.getPictureURL()!!){ imageResult ->
                            if(imageResult is Result.ImageSuccess){
                                user.setPicture(imageResult.bytes)
                                _searchState.value = null // to trigger recomposition
                                _searchState.value = result
                            }
                        }
                    }
                }
            }
        }
    }

}