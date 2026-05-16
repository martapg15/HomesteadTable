package dam_a51564.homesteadtable.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dam_a51564.homesteadtable.data.RecipeRepository
import dam_a51564.homesteadtable.model.Recipe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    // Keep a master copy to filter against without losing data
    private var masterRecipeList: List<Recipe> = emptyList()

    init {
        // Collect recipes from the repository as they change
        viewModelScope.launch {
            RecipeRepository.recipes.collect { recipeList ->
                masterRecipeList = recipeList
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

        val filteredRecipes = masterRecipeList.filter { recipe ->
            val matchesSearch = recipe.title.contains(query, ignoreCase = true)
            val matchesCategory = category == "All" || recipe.category.equals(category, ignoreCase = true)
            matchesSearch && matchesCategory
        }

        _uiState.update { state ->
            state.copy(
                recipes = filteredRecipes,
                favorites = masterRecipeList.filter { it.isFavourite }
            )
        }
    }
}