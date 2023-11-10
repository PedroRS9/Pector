package es.ulpgc.pamn.pector.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class FirebaseUserRepository : UserRepository {
    override fun createUser(user: User, callback: (Result) -> Unit) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword("pruebapruebita@gmail.com", "123456").addOnCompleteListener{
            if(it.isSuccessful){
                println("Registro satisfactorio");
                val firebaseUser: FirebaseUser? = it.result.user
                val user = if (firebaseUser != null) {
                    User(
                        firebaseUser.displayName,
                        null,
                        firebaseUser.email,
                        firebaseUser.photoUrl?.toString()
                    )
                } else{
                    null
                }

            } else{
                println("Registro no satisfactorio")
                it.exception?.let{exception ->
                    println("Error: ${exception.message}")
                }
            }
        }
    }
}