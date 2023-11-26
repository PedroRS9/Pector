package es.ulpgc.pamn.pector.data

import java.io.InputStream

interface ImageRepository {
    fun uploadImage(filename: String, byteArray: ByteArray, callback: (Result) -> Unit)
    fun downloadImage(pictureURL: String, callback: (Result) -> Unit)
}