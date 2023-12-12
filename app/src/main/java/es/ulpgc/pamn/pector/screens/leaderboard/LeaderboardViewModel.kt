package es.ulpgc.pamn.pector.screens.leaderboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import es.ulpgc.pamn.pector.data.FirebaseImageRepository
import es.ulpgc.pamn.pector.data.FirebaseLeaderboardRepository
import es.ulpgc.pamn.pector.data.FirebaseUserRepository
import es.ulpgc.pamn.pector.data.Result

class LeaderboardViewModel : ViewModel() {

    private val leaderboardRepository = FirebaseLeaderboardRepository()
    private var _leaderboardState = MutableLiveData<Result>()
    val leaderboardState: LiveData<Result> = _leaderboardState

    private val userRepository = FirebaseUserRepository()
    private val imageRepository = FirebaseImageRepository()

    fun getLeaderboard(gameType: String) {
        leaderboardRepository.getLeaderboard(gameType) { result: Result ->
            _leaderboardState.value = result
            if (result is Result.LeaderboardSuccess) {
                onLeaderboardDownloaded()
            }
        }
    }

    fun onLeaderboardDownloaded() {
        val topScores = (_leaderboardState.value as Result.LeaderboardSuccess).leaderboard
        for (topScore in topScores) {
            userRepository.findUserByUsername(topScore.username) {
                if (it != null) {
                    val userProfilePicture = it.getPictureURL()
                    if (userProfilePicture != null) {
                        imageRepository.downloadImage(userProfilePicture) { result: Result ->
                            if (result is Result.ImageSuccess) {
                                topScore.profilePicture = result.bytes
                                _leaderboardState.value = null // we set it to null to trigger a recomposition
                                _leaderboardState.value = Result.LeaderboardSuccess(topScores)
                            }
                        }
                    }
                }
            }
        }
    }
}