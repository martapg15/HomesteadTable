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

class EditRecipeViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(EditRecipeUiState())
    val uiState: StateFlow<EditRecipeUiState> = _uiState.asStateFlow()

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
    fun onTitleChange(title: String) = _uiState.update {
        it.copy(recipe = it.recipe.copy(title = title), errorMessage = null)
    }

    fun onServingsChange(servings: Int) = _uiState.update {
        it.copy(recipe = it.recipe.copy(baseServings = servings))
    }

    fun onCategorySelect(category: String) = _uiState.update {
        it.copy(recipe = it.recipe.copy(category = category), isCategoryExpanded = false)
    }

    fun onCategoryExpandedChange(expanded: Boolean) = _uiState.update {
        it.copy(isCategoryExpanded = expanded)
    }

    // Ingredient Helpers
    fun addIngredient() = _uiState.update { state ->
        state.copy(recipe = state.recipe.copy(ingredients = state.recipe.ingredients + Ingredient()))
    }

    fun updateIngredient(index: Int, ingredient: Ingredient) = _uiState.update { state ->
        val newList = state.recipe.ingredients.toMutableList()
        if (index in newList.indices) {
            newList[index] = ingredient
        }
        state.copy(recipe = state.recipe.copy(ingredients = newList))
    }

    fun removeIngredient(index: Int) = _uiState.update { state ->
        val newList = state.recipe.ingredients.toMutableList().apply {
            if (size > 1) removeAt(index)
        }
        state.copy(recipe = state.recipe.copy(ingredients = newList))
    }

    // Equipment Helpers
    fun addEquipment() = _uiState.update { state ->
        state.copy(recipe = state.recipe.copy(equipment = state.recipe.equipment + ""))
    }

    fun updateEquipment(index: Int, name: String) = _uiState.update { state ->
        val newList = state.recipe.equipment.toMutableList()
        if (index in newList.indices) {
            newList[index] = name
        }
        state.copy(recipe = state.recipe.copy(equipment = newList))
    }

    fun removeEquipment(index: Int) = _uiState.update { state ->
        val newList = state.recipe.equipment.toMutableList().apply {
            if (size > 1) removeAt(index)
        }
        state.copy(recipe = state.recipe.copy(equipment = newList))
    }

    // Instruction Helpers
    fun addInstruction() = _uiState.update { state ->
        state.copy(recipe = state.recipe.copy(instructions = state.recipe.instructions + ""))
    }

    fun updateInstruction(index: Int, text: String) = _uiState.update { state ->
        val newList = state.recipe.instructions.toMutableList()
        if (index in newList.indices) {
            newList[index] = text
        }
        state.copy(recipe = state.recipe.copy(instructions = newList))
    }

    fun removeInstruction(index: Int) = _uiState.update { state ->
        val newList = state.recipe.instructions.toMutableList().apply {
            if (size > 1) removeAt(index)
        }
        state.copy(recipe = state.recipe.copy(instructions = newList))
    }

    fun onImageSelected(uri: Uri) {
        _uiState.update { it.copy(imageUri = uri) }
    }

    // Helper function to explicitly validate user's inputs
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

    // Save/Update Logic
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