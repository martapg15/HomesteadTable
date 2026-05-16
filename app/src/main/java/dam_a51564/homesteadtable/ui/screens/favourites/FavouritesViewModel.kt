package dam_a51564.homesteadtable.ui.screens.favourites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dam_a51564.homesteadtable.data.RecipeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FavouritesViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(FavouritesUiState())
    val uiState: StateFlow<FavouritesUiState> = _uiState.asStateFlow()

    init {
        // Collect from Repository and automatically filter for favourites
        viewModelScope.launch {
            RecipeRepository.recipes.collect { list ->
                _uiState.update { it.copy(favouriteRecipes = list.filter { recipe -> recipe.isFavourite }) }
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