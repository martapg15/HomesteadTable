package dam_a51564.homesteadtable.ui.screens.favourites

import dam_a51564.homesteadtable.model.Recipe
import dam_a51564.homesteadtable.model.RecipeCategories

/**
 * UI state representing the active data and filter configurations on the Favourites screen.
 *
 * @property searchQuery The current text filter applied by the user.
 * @property selectedCategory The currently active category filter (defaults to "All").
 * @property categories The list of available category filters.
 * @property favouriteRecipes The active list of favorite recipes matching current search and category filters.
 * @property isLoading True when data is being fetched.
 */
data class FavouritesUiState(
    val searchQuery: String = "",
    val selectedCategory: String = "All",
    val categories: List<String> = RecipeCategories.filterList,
    val favouriteRecipes: List<Recipe> = emptyList(),
    val isLoading: Boolean = false
)