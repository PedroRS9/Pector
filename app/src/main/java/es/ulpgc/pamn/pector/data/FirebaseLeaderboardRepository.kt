package es.ulpgc.pamn.pector.data

import com.google.firebase.Firebase
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore

class FirebaseLeaderboardRepository : LeaderboardRepository{

    private val database = Firebase.firestore
    override fun getLeaderboard(gameType: String, callback: (Result) -> Unit) {
        database.collection("game_records")
            .document(gameType)
            .collection("top_scores")
            .orderBy("score", Query.Direction.DESCENDING)
            .limit(10)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val topScores = querySnapshot.documents.map { document ->
                    val date = document.getTimestamp("date") ?: ""
                    val score = document.getLong("score")?.toInt() ?: -1
                    val username = document.getString("username") ?: ""
                    TopScore(username = username, score = score, date = date.toString(), gameType = gameType)
                }
                callback(Result.LeaderboardSuccess(topScores))
            }
            .addOnFailureListener { exception ->
                callback(Result.Error(exception))
            }
    }

}
