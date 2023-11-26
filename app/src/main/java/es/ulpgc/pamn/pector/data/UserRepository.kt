package es.ulpgc.pamn.pector.data

interface UserRepository {
    fun createUser(user: User, callback: (Result) -> (Unit))
    fun findUserByUsername(username: String, callback: (User?) -> Unit)
    fun findUserByEmail(email: String, callback: (User?) -> Unit)
    fun updateUser(user: User, callback: (Result) -> Unit)
}