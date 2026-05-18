package dam_a51564.homesteadtable.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dam_a51564.homesteadtable.model.Recipe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await

object RecipeRepository {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val recipesCollection = db.collection("recipes")

    private val _recipes = MutableStateFlow<List<Recipe>>(emptyList())
    val recipes: StateFlow<List<Recipe>> = _recipes.asStateFlow()

    init {
        // Automatically listen for the logged-in user
        auth.addAuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                startListeningToUserRecipes(user.uid)
            } else {
                _recipes.value = emptyList() // Clear data when user logs out
            }
        }
    }

    private fun startListeningToUserRecipes(uid: String) {
        // Query Firestore for recipes belonging ONLY to the logged-in user
        recipesCollection.whereEqualTo("userId", uid)
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) {
                    return@addSnapshotListener
                }

                // Magically convert Firestore documents back into Kotlin Recipe objects
                val recipeList = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(Recipe::class.java)
                }
                _recipes.value = recipeList
            }
    }

    suspend fun addRecipe(recipe: Recipe) {
        val uid = auth.currentUser?.uid ?: return
        val recipeWithOwner = recipe.copy(userId = uid) // Attach the user ID

        // Use the generated UUID as the document name in Firestore
        recipesCollection.document(recipeWithOwner.id).set(recipeWithOwner).await()
    }

    suspend fun updateRecipe(recipe: Recipe) {
        recipesCollection.document(recipe.id).set(recipe).await()
    }

    suspend fun deleteRecipe(recipeId: String) {
        recipesCollection.document(recipeId).delete().await()
    }

    suspend fun toggleFavourite(recipeId: String) {
        // Find current state, flip it, and update Firestore
        val currentRecipe = _recipes.value.find { it.id == recipeId } ?: return
        val updatedRecipe = currentRecipe.copy(isFavourite = !currentRecipe.isFavourite)
        updateRecipe(updatedRecipe)
    }

    fun getRecipeById(id: String): Recipe? {
        return _recipes.value.find { it.id == id }
    }
}