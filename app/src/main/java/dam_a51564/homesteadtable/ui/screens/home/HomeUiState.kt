package dam_a51564.homesteadtable.ui.screens.home

import dam_a51564.homesteadtable.model.Recipe
import dam_a51564.homesteadtable.model.RecipeCategories

/**
 * UI state representing the active data and filter configurations on the Home screen.
 *
 * @property searchQuery The current text filter applied by the user.
 * @property selectedCategory The currently active category filter (defaults to "All").
 * @property categories The list of available category filters.
 * @property recipes The active list of recipes matching current search and category filters.
 * @property favorites A filtered sub-list containing only the user's favorite recipes.
 * @property isLoading True when initial data synchronization is in progress.
 */
data class HomeUiState(
    val searchQuery: String = "",
    val selectedCategory: String = "All",
    val categories: List<String> = RecipeCategories.filterList,
    val recipes: List<Recipe> = emptyList(),
    val favorites: List<Recipe> = emptyList(),
    val isLoading: Boolean = false
)