package dam_a51564.homesteadtable.ui.screens.favourites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dam_a51564.homesteadtable.data.RecipeRepository
import dam_a51564.homesteadtable.model.Recipe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FavouritesViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(FavouritesUiState())
    val uiState: StateFlow<FavouritesUiState> = _uiState.asStateFlow()

    private var masterRecipeList: List<Recipe> = emptyList()

    init {
        // Collect from Repository and automatically filter for favourites
        viewModelScope.launch {
            RecipeRepository.recipes.collect { list ->
                masterRecipeList = list
                applyFilter()
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        applyFilter()
    }

    fun onCategorySelect(category: String) {
        _uiState.update { it.copy(selectedCategory = category) }
        applyFilter()
    }

    fun toggleFavourite(recipeId: String) {
        RecipeRepository.toggleFavourite(recipeId)
    }

    private fun applyFilter() {
        val currentState = _uiState.value
        val query = currentState.searchQuery
        val category = currentState.selectedCategory

        val filteredFavorites = masterRecipeList.filter { recipe ->
            val matchesSearch = recipe.title.contains(query, ignoreCase = true)
            val matchesCategory = category == "All" || recipe.category.equals(category, ignoreCase = true)

            // Must be a favourite AND match the search/category filters
            recipe.isFavourite && matchesSearch && matchesCategory
        }

        _uiState.update { it.copy(favouriteRecipes = filteredFavorites) }
    }
}