package dam_a51564.homesteadtable.ui.screens.recipe_management

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dam_a51564.homesteadtable.data.RecipeRepository
import dam_a51564.homesteadtable.model.Ingredient
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddRecipeViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AddRecipeUiState())
    val uiState: StateFlow<AddRecipeUiState> = _uiState.asStateFlow()

    // Basic Info
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
    fun addIngredient() = _uiState.update {
        it.copy(recipe = it.recipe.copy(ingredients = it.recipe.ingredients + Ingredient()))
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
    fun addEquipment() = _uiState.update {
        it.copy(recipe = it.recipe.copy(equipment = it.recipe.equipment + ""))
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
    fun addInstruction() = _uiState.update {
        it.copy(recipe = it.recipe.copy(instructions = it.recipe.instructions + ""))
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

    // Save Logic
    fun onSaveRecipe() {
        val recipe = _uiState.value.recipe

        // Basic Validation
        if (recipe.title.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Please enter a title.") }
            return
        }

        _uiState.update { it.copy(isSaving = true) }

        viewModelScope.launch {
            delay(800) // Simulating save time
            RecipeRepository.addRecipe(recipe)
            _uiState.update { it.copy(isSaving = false, isSaved = true) }
        }
    }
}