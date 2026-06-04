package dam_a51564.homesteadtable.ui.screens.recipe_management

import android.net.Uri
import dam_a51564.homesteadtable.model.Recipe
import dam_a51564.homesteadtable.model.RecipeCategories

/**
 * UI state tracking the form inputs and submission status while editing an existing recipe.
 *
 * @property recipe The [Recipe] object currently being modified.
 * @property categories The list of available recipe categories.
 * @property isCategoryExpanded True if the category selection dropdown is currently visible.
 * @property isLoading True when the existing recipe data is initially being fetched.
 * @property isSaving True when the updated recipe is actively being uploaded to Firestore.
 * @property isSaved True when the recipe update process completes successfully.
 * @property errorMessage Notification text explaining any form validation or network errors.
 * @property imageUri The local Android [Uri] of a newly selected image, if updated by the user.
 */
data class EditRecipeUiState(
    val recipe: Recipe = Recipe(),
    val categories: List<String> = RecipeCategories.list,
    val isCategoryExpanded: Boolean = false,
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val isSaved: Boolean = false,
    val errorMessage: String? = null,
    val imageUri: Uri? = null
)