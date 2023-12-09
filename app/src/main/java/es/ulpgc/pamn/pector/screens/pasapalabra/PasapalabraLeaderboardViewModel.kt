package es.ulpgc.pamn.pector.screens.pasapalabra

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import es.ulpgc.pamn.pector.data.FirebaseImageRepository
import es.ulpgc.pamn.pector.data.FirebaseLeaderboardRepository
import es.ulpgc.pamn.pector.data.FirebaseUserRepository
import es.ulpgc.pamn.pector.data.Result

class PasapalabraLeaderboardViewModel: ViewModel() {

    private val leaderboardRepository = FirebaseLeaderboardRepository()
    private var _leaderboardState = MutableLiveData<Result>()
    val leaderboardState: LiveData<Result> = _leaderboardState

    private val userRepository = FirebaseUserRepository()

    private val imageRepository = FirebaseImageRepository()

    fun getLeaderboard(){
        leaderboardRepository.getLeaderboard("pasapalabra"){ result: Result ->
            _leaderboardState.value = result
            if(result is Result.LeaderboardSuccess){
                onLeaderboardDownloaded()
            }
        }
    }

    fun onLeaderboardDownloaded(){
        // we iterate through the list of top scores and download each user's profile picture
        val topScores = (_leaderboardState.value as Result.LeaderboardSuccess).leaderboard
        for (topScore in topScores){
            userRepository.findUserByUsername(topScore.username){
                if(it != null){
                    val userProfilePicture = it.getPictureURL()
                    if(userProfilePicture != null){
                        imageRepository.downloadImage(userProfilePicture){ result: Result ->
                            if(result is Result.ImageSuccess){
                                topScore.profilePicture = result.bytes
                                _leaderboardState.value = Result.LeaderboardSuccess(topScores)
                            }
                        }
                    }
                }
            }
        }
    }
}