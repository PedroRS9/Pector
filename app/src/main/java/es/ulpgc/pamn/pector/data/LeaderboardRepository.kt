package es.ulpgc.pamn.pector.data

interface LeaderboardRepository {
    fun getLeaderboard(gameType: String, callback: (Result) -> Unit)
}