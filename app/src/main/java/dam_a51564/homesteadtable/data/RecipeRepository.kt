package dam_a51564.homesteadtable.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dam_a51564.homesteadtable.model.Recipe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await

/**
 * Repository object that manages recipes. Communicates with Firebase Firestore to sync,
 * add, update, delete, and filter recipes based on the currently logged-in user.
 */
object RecipeRepository {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val recipesCollection = db.collection("recipes")

    private val _recipes = MutableStateFlow<List<Recipe>>(emptyList())
    /**
     * Exposed [StateFlow] streaming the list of recipes belonging to the logged-in user.
     */
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

    /**
     * Sets up a real-time snapshot listener on the Firestore recipes collection,
     * filtering for records that match the current user's unique ID.
     *
     * @param uid The authenticated user's unique identifier.
     */
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

    /**
     * Adds a new recipe to Firestore. Attaches the current user's ID as the owner.
     *
     * @param recipe The [Recipe] instance to be saved.
     */
    suspend fun addRecipe(recipe: Recipe) {
        val uid = auth.currentUser?.uid ?: return
        val recipeWithOwner = recipe.copy(userId = uid) // Attach the user ID

        // Use the generated UUID as the document name in Firestore
        recipesCollection.document(recipeWithOwner.id).set(recipeWithOwner).await()
    }

    /**
     * Overwrites or updates an existing recipe document in Firestore.
     *
     * @param recipe The [Recipe] instance with updated fields.
     */
    suspend fun updateRecipe(recipe: Recipe) {
        recipesCollection.document(recipe.id).set(recipe).await()
    }

    /**
     * Deletes a recipe document from Firestore by its unique document ID.
     *
     * @param recipeId The unique ID of the recipe to delete.
     */
    suspend fun deleteRecipe(recipeId: String) {
        recipesCollection.document(recipeId).delete().await()
    }

    /**
     * Toggles the favorite status flag for a specific recipe and saves the update to Firestore.
     *
     * @param recipeId The unique ID of the recipe to toggle.
     */
    suspend fun toggleFavourite(recipeId: String) {
        // Find current state, flip it, and update Firestore
        val currentRecipe = _recipes.value.find { it.id == recipeId } ?: return
        val updatedRecipe = currentRecipe.copy(isFavourite = !currentRecipe.isFavourite)
        updateRecipe(updatedRecipe)
    }

    /**
     * Helper function to find a cached recipe locally by its unique identifier.
     *
     * @param id The unique ID of the recipe.
     * @return The matched [Recipe] object, or null if it cannot be found.
     */
    fun getRecipeById(id: String): Recipe? {
        return _recipes.value.find { it.id == id }
    }
}