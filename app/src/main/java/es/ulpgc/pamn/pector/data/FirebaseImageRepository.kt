package es.ulpgc.pamn.pector.data

import com.google.firebase.storage.FirebaseStorage

class FirebaseImageRepository : ImageRepository {
    private val storage = FirebaseStorage.getInstance()
    override fun uploadImage(filename: String, bytearray: ByteArray, callback: (Result) -> Unit) {
        val storageRef = storage.reference
        val imageRef = storageRef.child("profile-pictures/${filename}")
        val uploadTask = imageRef.putBytes(bytearray)
        uploadTask.addOnFailureListener {
            callback(Result.Error(it))
        }.addOnSuccessListener {
            downloadImage("profile-pictures/${filename}"){ result: Result ->
                callback(result)
            }
        }
    }

    override fun downloadImage(pictureURL: String, callback: (Result) -> Unit) {
        val storageRef = storage.reference
        val imageRef = storageRef.child(pictureURL)
        val ONE_MEGABYTE: Long = 1024 * 1024
        imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener { bytes ->
            callback(Result.ImageSuccess(bytes))
        }.addOnFailureListener {
            callback(Result.Error(it))
        }
    }
}