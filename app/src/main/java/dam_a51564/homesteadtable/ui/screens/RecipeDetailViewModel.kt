package dam_a51564.homesteadtable.ui.screens

import androidx.lifecycle.ViewModel
import dam_a51564.homesteadtable.data.RecipeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class RecipeDetailViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(RecipeDetailUiState())
    val uiState: StateFlow<RecipeDetailUiState> = _uiState.asStateFlow()

    // Pass the ID from navigation to load the correct recipe
    fun loadRecipe(recipeId: String) {
        val recipe = RecipeRepository.getRecipeById(recipeId)
        if (recipe != null) {
            _uiState.update {
                it.copy(
                    recipe = recipe,
                    currentServings = recipe.baseServings,
                    isLoading = false
                )
            }
        } else {
            _uiState.update { it.copy(isLoading = false, errorMessage = "Recipe not found") }
        }
    }

    fun deleteCurrentRecipe() {
        val recipeId = _uiState.value.recipe?.id ?: return
        RecipeRepository.deleteRecipe(recipeId)
        _uiState.update { it.copy(isDeleted = true) }
    }

    // Function triggered by the UI buttons to switch between lists
    fun selectTab(tab: DetailTab) {
        _uiState.update { it.copy(selectedTab = tab) }
    }

    fun onIncrementServings() = _uiState.update { it.copy(currentServings = it.currentServings + 1) }

    fun onDecrementServings() {
        if (_uiState.value.currentServings > 1) {
            _uiState.update { it.copy(currentServings = it.currentServings - 1) }
        }
    }

    fun getScaledQuantity(baseQuantityStr: String): String {
        val state = _uiState.value
        val recipe = state.recipe ?: return baseQuantityStr

        val baseQuantity = baseQuantityStr.toDoubleOrNull() ?: return baseQuantityStr

        val scaled = (baseQuantity * state.currentServings) / recipe.baseServings

        // Format to remove .0 if it's a whole number
        return if (scaled % 1.0 == 0.0) scaled.toInt().toString() else String.format("%.1f", scaled)
    }
}