package es.salvaaoliiver.secondevproject.main.database

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object RecipesRepository {

    private lateinit var userID: String
    fun setUserId(id: String) {
        this.userID = id
    }
    fun getUserId(): String {
        return userID
    }

    // GET RECIPES
    suspend fun loadRecipes(userId: String): MutableList<Recipe> {
        val recipes = mutableListOf<Recipe>()
        try {
            val query = FirebaseFirestore.getInstance()
                .collection("recetas")
                .whereEqualTo("usuarioId", userId)
                .get()
                .await()

            for (document in query.documents) {
                val recipe = Recipe(
                    document["titulo"].toString(),
                    document["pasos"].toString(),
                    document["imagenURL"].toString(),
                    false
                )
                recipes.add(recipe)
            }
        } catch (e: Exception) {
            e.stackTrace
        }
        return recipes
    }

    // GET USER BY EMAIL
    suspend fun getUserIdByEmail(email: String): String? {
        try {
            val query = FirebaseFirestore.getInstance()
                .collection("usuarios")
                .whereEqualTo("correoElectronico", email)
                .get()
                .await()

            return if (!query.isEmpty) {
                query.documents[0].id
            } else {
                val newUser = hashMapOf(
                    "correoElectronico" to email
                )
                val newUserRef = FirebaseFirestore.getInstance()
                    .collection("usuarios")
                    .add(newUser)
                    .await()
                newUserRef.id
            }
        } catch (e: Exception) {
            e.stackTrace
            return null
        }
    }
}