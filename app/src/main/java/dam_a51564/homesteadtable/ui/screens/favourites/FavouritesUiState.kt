package dam_a51564.homesteadtable.ui.screens.favourites

import dam_a51564.homesteadtable.model.Recipe

data class FavouritesUiState(
    val searchQuery: String = "",
    val selectedCategory: String = "All",
    val categories: List<String> = listOf("All", "Pasta", "Seafood", "Dessert", "Breakfast"),
    val favouriteRecipes: List<Recipe> = emptyList(),
    val isLoading: Boolean = false
)