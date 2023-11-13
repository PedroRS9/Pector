package es.ulpgc.pamn.pector.data

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.firestore

class FirebaseUserRepository : UserRepository {
    private val database = Firebase.firestore
    override fun createUser(user: User, callback: (Result) -> Unit) {
        findUserByUsername(user.getName()){ existingUser ->
            if(existingUser != null){
                callback(Result.Error(Exception("El nombre de usuario ${user.getName()} ya está en uso.")))
                return@findUserByUsername
            }
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(user.getEmail(), user.getPassword()).addOnCompleteListener{
                if(it.isSuccessful){
                    val firebaseUser = it.result?.user
                    val userMap = hashMapOf(
                        "username" to user.getName(),
                        "email" to user.getEmail()
                    )
                    if (firebaseUser != null) {
                        try {
                            database.collection("users").document(firebaseUser.uid).set(userMap)
                            callback(Result.Success(true))
                        } catch (e: Exception) {
                            callback(Result.Error(e))
                        }
                    } else{
                        callback(Result.Error(Exception("Error desconocido")))
                    }
                } else{
                    it.exception?.let{exception ->
                        callback(Result.Error(exception))
                    }
                }
            }
        }
    }

    override fun findUserByUsername(username: String, callback: (User?) -> Unit) {
        database.collection("users").whereEqualTo("username", username).get().addOnSuccessListener { documents ->
            if (documents.isEmpty) {
                callback(null)
            } else {
                val document = documents.documents[0]
                val user = User(
                    name = document.getString("username") ?: "",
                    password = "", // we don't store passwords in firestore
                    email = document.getString("email") ?: "",
                    pictureURL = document.getString("pictureURL")
                )
                callback(user)
            }
        }
    }

}