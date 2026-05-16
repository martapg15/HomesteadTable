package dam_a51564.homesteadtable.ui.screens.favourites

import dam_a51564.homesteadtable.model.Recipe
import dam_a51564.homesteadtable.model.RecipeCategories

data class FavouritesUiState(
    val searchQuery: String = "",
    val selectedCategory: String = "All",
    val categories: List<String> = RecipeCategories.filterList,
    val favouriteRecipes: List<Recipe> = emptyList(),
    val isLoading: Boolean = false
)