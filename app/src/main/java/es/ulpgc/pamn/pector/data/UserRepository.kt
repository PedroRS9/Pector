package es.ulpgc.pamn.pector.data

interface UserRepository {
    fun createUser(user: User, callback: (Result) -> (Unit))
}