package dam_a51564.homesteadtable.ui.screens.recipe_management

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dam_a51564.homesteadtable.data.ImageRepository
import dam_a51564.homesteadtable.data.RecipeRepository
import dam_a51564.homesteadtable.model.Ingredient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel responsible for managing the Edit Recipe screen.
 * Handles loading existing recipe data, validating form updates, and saving changes to Firestore.
 */
class EditRecipeViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(EditRecipeUiState())
    val uiState: StateFlow<EditRecipeUiState> = _uiState.asStateFlow()

    /**
     * Fetches an existing recipe from the repository by its identifier to populate the edit form fields.
     *
     * @param recipeId The unique document identifier of the target recipe in Firestore.
     */
    fun loadRecipe(recipeId: String) {
        val existingRecipe = RecipeRepository.getRecipeById(recipeId)
        if (existingRecipe != null) {
            _uiState.update {
                it.copy(
                    recipe = existingRecipe,
                    isLoading = false
                )
            }
        } else {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    errorMessage = "Recipe not found."
                )
            }
        }
    }

    // Basic Info Updates
    /**
     * Updates the recipe title state entry following user keystrokes in the editing form.
     *
     * @param title The newly updated title string.
     */
    fun onTitleChange(title: String) = _uiState.update {
        it.copy(recipe = it.recipe.copy(title = title), errorMessage = null)
    }

    /**
     * Updates the base serving size for the recipe.
     *
     * @param servings Portion index count targets.
     */
    fun onServingsChange(servings: Int) = _uiState.update {
        it.copy(recipe = it.recipe.copy(baseServings = servings))
    }

    /**
     * Updates the selected category and closes the dropdown menu.
     *
     * @param category Title labels describing culinary classes.
     */
    fun onCategorySelect(category: String) = _uiState.update {
        it.copy(recipe = it.recipe.copy(category = category), isCategoryExpanded = false)
    }

    /**
     * Toggles the visibility of the category dropdown menu.
     *
     * @param expanded Visual display state tracking variables.
     */
    fun onCategoryExpandedChange(expanded: Boolean) = _uiState.update {
        it.copy(isCategoryExpanded = expanded)
    }

    /**
     * Updates the selected image URI for the recipe.
     *
     * @param uri Local path targeting storage devices.
     */
    fun onImageSelected(uri: Uri) {
        _uiState.update { it.copy(imageUri = uri) }
    }

    // Ingredient Helpers
    /**
     * Adds a new blank ingredient to the recipe draft.
     */
    fun addIngredient() = _uiState.update { state ->
        state.copy(recipe = state.recipe.copy(ingredients = state.recipe.ingredients + Ingredient()))
    }

    /**
     * Updates a specific ingredient in the recipe draft at the given index.
     *
     * @param index The position of the ingredient in the list to be modified.
     * @param ingredient The updated ingredient data structure containing new values.
     */
    fun updateIngredient(index: Int, ingredient: Ingredient) = _uiState.update { state ->
        val newList = state.recipe.ingredients.toMutableList()
        if (index in newList.indices) {
            newList[index] = ingredient
        }
        state.copy(recipe = state.recipe.copy(ingredients = newList))
    }

    /**
     * Removes the ingredient at the specified index, ensuring at least one remains.
     *
     * @param index The position of the ingredient to be removed.
     */
    fun removeIngredient(index: Int) = _uiState.update { state ->
        val newList = state.recipe.ingredients.toMutableList().apply {
            if (size > 1) removeAt(index)
        }
        state.copy(recipe = state.recipe.copy(ingredients = newList))
    }

    // Equipment Helpers
    /**
     * Adds a new blank equipment item to the recipe draft.
     */
    fun addEquipment() = _uiState.update { state ->
        state.copy(recipe = state.recipe.copy(equipment = state.recipe.equipment + ""))
    }

    /**
     * Updates the equipment item at the specified index.
     *
     * @param index The position of the equipment entry in the list to be modified.
     * @param name The new name string for the equipment item.
     */
    fun updateEquipment(index: Int, name: String) = _uiState.update { state ->
        val newList = state.recipe.equipment.toMutableList()
        if (index in newList.indices) {
            newList[index] = name
        }
        state.copy(recipe = state.recipe.copy(equipment = newList))
    }

    /**
     * Removes the equipment item at the specified index, ensuring at least one remains.
     *
     * @param index The position of the equipment item to be removed.
     */
    fun removeEquipment(index: Int) = _uiState.update { state ->
        val newList = state.recipe.equipment.toMutableList().apply {
            if (size > 1) removeAt(index)
        }
        state.copy(recipe = state.recipe.copy(equipment = newList))
    }

    // Instruction Helpers
    /**
     * Adds a new blank instruction step to the recipe draft.
     */
    fun addInstruction() = _uiState.update { state ->
        state.copy(recipe = state.recipe.copy(instructions = state.recipe.instructions + ""))
    }

    /**
     * Updates the instruction step at the specified index.
     *
     * @param index The position of the instruction step in the list to be modified.
     * @param text The new narrative content string describing the step.
     */
    fun updateInstruction(index: Int, text: String) = _uiState.update { state ->
        val newList = state.recipe.instructions.toMutableList()
        if (index in newList.indices) {
            newList[index] = text
        }
        state.copy(recipe = state.recipe.copy(instructions = newList))
    }

    /**
     * Removes the instruction step at the specified index, ensuring at least one remains.
     *
     * @param index The position of the instruction step to be removed.
     */
    fun removeInstruction(index: Int) = _uiState.update { state ->
        val newList = state.recipe.instructions.toMutableList().apply {
            if (size > 1) removeAt(index)
        }
        state.copy(recipe = state.recipe.copy(instructions = newList))
    }

    /**
     * Helper function to explicitly validate user's inputs before saving.
     */
    private fun validateInputs(): String? {
        val state = _uiState.value
        val recipe = state.recipe

        // Basic Info
        if (recipe.title.isBlank()) return "Please enter a recipe title."
        if (recipe.category.isBlank()) return "Please select a category."

        // Image (They either picked a new one, or the recipe already has one)
        if (state.imageUri == null && recipe.imageUrl.isBlank()) return "Please add a photo of your recipe."

        // At least one valid ingredient (has a name and a quantity)
        val hasValidIngredient = recipe.ingredients.any {
            it.name.isNotBlank() && it.quantity.isNotBlank()
        }
        if (!hasValidIngredient) return "Please add at least one complete ingredient."

        // At least one valid equipment item
        val hasValidEquipment = recipe.equipment.any { it.isNotBlank() }
        if (!hasValidEquipment) return "Please add at least one piece of equipment."

        // At least one valid instruction step
        val hasValidInstruction = recipe.instructions.any { it.isNotBlank() }
        if (!hasValidInstruction) return "Please add at least one instruction step."

        // If all checks pass, return null (meaning no errors)
        return null
    }

    /**
     * Processes modifications applied to the active recipe. If a new local image URI is specified,
     * it uploads the image asset to Cloudinary before performing an overwrite update in Firestore.
     */
    fun onUpdateRecipe() {
        val validationError = validateInputs()

        // Stop immediately if validation fails and show the specific error message
        if (validationError != null) {
            _uiState.update { it.copy(errorMessage = validationError) }
            return
        }

        _uiState.update { it.copy(isSaving = true, errorMessage = null) }

        viewModelScope.launch {
            try {
                val state = _uiState.value
                // Default to the existing URL
                var finalImageUrl = state.recipe.imageUrl

                // If the user selected a new image, upload it to Cloudinary and overwrite the variable
                if (state.imageUri != null) {
                    finalImageUrl = ImageRepository.uploadImage(state.imageUri)
                }

                val currentRecipe = state.recipe

                // Filter out any blank fields the user left empty before saving!
                val cleanRecipe = currentRecipe.copy(
                    imageUrl = finalImageUrl,
                    ingredients = currentRecipe.ingredients.filter { it.name.isNotBlank() && it.quantity.isNotBlank() },
                    equipment = currentRecipe.equipment.filter { it.isNotBlank() },
                    instructions = currentRecipe.instructions.filter { it.isNotBlank() }
                )

                // Save to Firestore
                RecipeRepository.updateRecipe(cleanRecipe)

                _uiState.update { it.copy(isSaving = false, isSaved = true) }
            } catch (e: Exception) {
                // If Cloudinary or Firestore fails, show the error
                _uiState.update { it.copy(isSaving = false, errorMessage = e.localizedMessage) }
            }
        }
    }
}