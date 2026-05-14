package dam_a51564.homesteadtable.ui.screens

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AddRecipeViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AddRecipeUiState())
    val uiState: StateFlow<AddRecipeUiState> = _uiState.asStateFlow()

    fun onTitleChange(newTitle: String) = _uiState.update { it.copy(title = newTitle) }

    // Ingredients Logic
    fun addIngredient() = _uiState.update { it.copy(ingredients = it.ingredients + Ingredient()) }

    fun updateIngredient(index: Int, ingredient: Ingredient) = _uiState.update { state ->
        val newList = state.ingredients.toMutableList()
        newList[index] = ingredient
        state.copy(ingredients = newList)
    }

    fun removeIngredient(index: Int) = _uiState.update { state ->
        if (state.ingredients.size > 1) state.copy(ingredients = state.ingredients.filterIndexed { i, _ -> i != index }) else state
    }

    // Equipment Logic
    fun addEquipment() = _uiState.update { it.copy(equipment = it.equipment + "") }

    fun updateEquipment(index: Int, name: String) = _uiState.update { state ->
        val newList = state.equipment.toMutableList()
        newList[index] = name
        state.copy(equipment = newList)
    }

    fun removeEquipment(index: Int) = _uiState.update { state ->
        if (state.equipment.size > 1) state.copy(equipment = state.equipment.filterIndexed { i, _ -> i != index }) else state
    }

    // Steps Logic
    fun addStep() = _uiState.update { it.copy(steps = it.steps + "") }

    fun updateStep(index: Int, text: String) = _uiState.update { state ->
        val newList = state.steps.toMutableList()
        newList[index] = text
        state.copy(steps = newList)
    }

    fun removeStep(index: Int) = _uiState.update { state ->
        if (state.steps.size > 1) state.copy(steps = state.steps.filterIndexed { i, _ -> i != index }) else state
    }

    fun onSaveRecipe() {
        if (_uiState.value.title.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Title is required") }
            return
        }
        // TODO: Save to Firebase
        _uiState.update { it.copy(isLoading = true) }
    }
}