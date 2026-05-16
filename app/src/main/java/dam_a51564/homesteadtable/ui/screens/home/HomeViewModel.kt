package dam_a51564.homesteadtable.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dam_a51564.homesteadtable.data.RecipeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        // Collect recipes from the repository as they change
        viewModelScope.launch {
            RecipeRepository.recipes.collect { recipeList ->
                _uiState.update { state ->
                    state.copy(
                        recipes = recipeList,
                        favorites = recipeList.filter { recipe -> recipe.isFavourite }
                    )
                }
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun onCategorySelect(category: String) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    fun toggleFavourite(recipeId: String) {
        RecipeRepository.toggleFavourite(recipeId)
    }
}