package dam_a51564.homesteadtable.ui.screens.recipe_management

import android.net.Uri
import dam_a51564.homesteadtable.model.Ingredient
import dam_a51564.homesteadtable.model.Recipe
import dam_a51564.homesteadtable.model.RecipeCategories

/**
 * UI state tracking the form inputs and submission status while creating a new recipe.
 *
 * @property recipe The draft [Recipe] object currently being composed.
 * @property categories The list of available recipe categories.
 * @property isCategoryExpanded True if the category selection dropdown is currently visible.
 * @property isSaving True when the recipe is actively being uploaded to Firestore.
 * @property isSaved True when the recipe creation process completes successfully.
 * @property errorMessage Notification text explaining any form validation or network errors.
 * @property imageUri The local Android [Uri] of the image selected for the recipe.
 */
data class AddRecipeUiState(
    val recipe: Recipe = Recipe(
        ingredients = listOf(Ingredient()),
        equipment = listOf(""),
        instructions = listOf("")
    ),
    val categories: List<String> = RecipeCategories.list,
    val isCategoryExpanded: Boolean = false,
    val isSaving: Boolean = false,
    val isSaved: Boolean = false,
    val errorMessage: String? = null,
    val imageUri: Uri? = null
)