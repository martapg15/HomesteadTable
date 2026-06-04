package dam_a51564.homesteadtable.ui.screens.favourites

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
 * ViewModel responsible for managing the Favourites screen.
 * Handles fetching, filtering, and toggling the favorite status of recipes.
 */
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

    /**
     * Updates the search query state and applies the filter.
     *
     * @param query The updated search keyword sequence.
     */
    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        applyFilter()
    }

    /**
     * Updates the selected category state and applies the filter.
     *
     * @param category The name string of the filter category selected.
     */
    fun onCategorySelect(category: String) {
        _uiState.update { it.copy(selectedCategory = category) }
        applyFilter()
    }

    /**
     * Toggles the favorite status of a recipe in the repository.
     *
     * @param recipeId The unique key identifying the target recipe configuration file.
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
     * Filters the cached primary master collection, validating query text terms and category restrictions,
     * ensuring only elements with active favorite parameter metrics remain visible.
     */
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