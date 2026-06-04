package dam_a51564.homesteadtable.ui.screens.recipe_management

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dam_a51564.homesteadtable.data.ImageRepository
import dam_a51564.homesteadtable.data.RecipeRepository
import dam_a51564.homesteadtable.model.Recipe
import dam_a51564.homesteadtable.model.Ingredient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel responsible for managing the UI state and business logic of the Add Recipe screen.
 * Handles form validation, image uploading via [ImageRepository], and saving new recipes to Firestore.
 */
class AddRecipeViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AddRecipeUiState())
    val uiState: StateFlow<AddRecipeUiState> = _uiState.asStateFlow()

    // Basic Info
    /**
     * Updates the title of the recipe being drafted and clears any existing validation error.
     *
     * @param title The new title text entered by the user.
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
    fun addIngredient() = _uiState.update {
        it.copy(recipe = it.recipe.copy(ingredients = it.recipe.ingredients + Ingredient()))
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
     * @param index Positional marker designating elements targeted for removal.
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
    fun addEquipment() = _uiState.update {
        it.copy(recipe = it.recipe.copy(equipment = it.recipe.equipment + ""))
    }

    /**
     * Updates the equipment item at the specified index.
     *
     * @param index Target row position markers.
     * @param name Target equipment values updating fields contextually.
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
     * @param index Positional parameters pointing to components.
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
    fun addInstruction() = _uiState.update {
        it.copy(recipe = it.recipe.copy(instructions = it.recipe.instructions + ""))
    }

    /**
     * Updates the instruction step at the specified index.
     *
     * @param index Target row position markers.
     * @param text Target instruction values updating fields contextually.
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
     * @param index Positional marker designating elements targeted for removal.
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

        // Image
        if (state.imageUri == null) return "Please add a photo of your recipe."

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
     * Validates the current recipe draft, uploads the selected image to Cloudinary,
     * filters out blank fields (ingredients, equipment, instructions), and commits
     * the final [Recipe] record to Firebase Firestore.
     */
    fun onSaveRecipe() {
        // Run our strict validation check
        val validationError = validateInputs()

        // Stop immediately if validation fails and show the specific error message
        if (validationError != null) {
            _uiState.update { it.copy(errorMessage = validationError) }
            return
        }

        // If valid, proceed with the upload and save process
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, errorMessage = null) }

            try {
                // Because of our validation, we know imageUri is NOT null here
                val currentUri = _uiState.value.imageUri!!

                // Upload to Cloudinary
                val uploadedUrl = ImageRepository.uploadImage(currentUri)

                val currentRecipe = _uiState.value.recipe

                // Filter out any blank fields the user left empty before saving
                val cleanRecipe = currentRecipe.copy(
                    imageUrl = uploadedUrl,
                    ingredients = currentRecipe.ingredients.filter { it.name.isNotBlank() && it.quantity.isNotBlank() },
                    equipment = currentRecipe.equipment.filter { it.isNotBlank() },
                    instructions = currentRecipe.instructions.filter { it.isNotBlank() }
                )

                // Save the clean recipe to Firebase
                RecipeRepository.addRecipe(cleanRecipe)

                // Update state on success
                _uiState.update { it.copy(isSaved = true, isSaving = false) }

            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "Failed to save: ${e.localizedMessage}", isSaving = false) }
            }
        }
    }
}