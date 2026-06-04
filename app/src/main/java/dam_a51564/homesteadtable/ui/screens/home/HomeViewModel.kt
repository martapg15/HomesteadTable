package dam_a51564.homesteadtable.ui.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dam_a51564.homesteadtable.data.RecipeRepository
import dam_a51564.homesteadtable.model.Recipe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel responsible for managing the Home screen.
 * Handles fetching recipes, applying search and category filters, and toggling favorite statuses.
 */
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

    /**
     * Updates the search query state and applies the filter.
     *
     * @param query Target text search keywords specified by users.
     */
    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        applyFilter()
    }

    /**
     * Updates the selected category state and applies the filter.
     *
     * @param category Title name reference for targeted selection categories.
     */
    fun onCategorySelect(category: String) {
        _uiState.update { it.copy(selectedCategory = category) }
        applyFilter()
    }

    /**
     * Toggles the favorite status of a recipe in the repository.
     *
     * @param recipeId Unique document identifier target key.
     */
    fun toggleFavourite(recipeId: String) {
        // Wrapped in a coroutine because toggleFavourite is now a suspend function
        viewModelScope.launch {
            try {
                RecipeRepository.toggleFavourite(recipeId)
            } catch (e: Exception) {
                Log.e("FavouriteError", "Failed to toggle favourite state", e)
            }
        }
    }

    /**
     * Filters the master recipe list based on the current search query and selected category.
     */
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