package es.salvaaoliiver.secondevproject.login

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import kotlinx.coroutines.tasks.await


// OBJECT para que solo exista una única instancia de AuthManager en toda la aplicación
object AuthManager {

    private val auth: FirebaseAuth by lazy {Firebase.auth}

    suspend fun login(email: String, password: String): FirebaseUser?{
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            authResult.user
        } catch (e: Exception) {
            null
        }
    }

    suspend fun register(email: String, password: String): FirebaseUser? {
        return try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            authResult.user
        } catch (e: Exception) {
            null
        }
    }

    @Throws(Exception::class)
    suspend fun sendPasswordResetEmail(email: String): Unit {
        auth.sendPasswordResetEmail(email).await()
    }

    fun logOut() {
        auth.signOut()
    }

    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }
}