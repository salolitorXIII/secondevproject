package es.salvaaoliiver.secondevproject.main.database

import android.util.Log
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

    // GET PUBLIC RECIPES
    suspend fun loadPublicRecipes(): List<Recipe> {
        val publicRecipes = mutableListOf<Recipe>()
        try {
            val query = FirebaseFirestore.getInstance()
                .collection("recetas")
                .whereEqualTo("publica", true)
                .get()
                .await()

            for (document in query.documents) {
                val recipe = Recipe(
                    title = document["titulo"].toString(),
                    steps = document["pasos"].toString(),
                    imagePath = document["imagenURL"].toString(),
                    public = document["publica"] as Boolean
                )
                publicRecipes.add(recipe)
            }
        } catch (e: Exception) {
            Log.e("RecipesRepository", "Error loading public recipes", e)
        }
        return publicRecipes
    }

    // ADD RECIPE
    suspend fun addRecipe(recipe: Recipe, onSuccess: () -> Unit) {
        val recetas = FirebaseFirestore.getInstance().collection("recetas")

        val newReceta = hashMapOf(
            "usuarioId" to userID,
            "titulo" to recipe.title,
            "pasos" to recipe.steps,
            "imagenURL" to recipe.imagePath,
            "publica" to recipe.public
        )

        recetas.add(newReceta)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> Log.e("Firestore", "Error adding recipe", e) }
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