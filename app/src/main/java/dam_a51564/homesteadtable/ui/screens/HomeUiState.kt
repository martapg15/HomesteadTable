package dam_a51564.homesteadtable.ui.screens

import dam_a51564.homesteadtable.model.Recipe

data class HomeUiState(
    val searchQuery: String = "",
    val selectedCategory: String = "All",
    val categories: List<String> = listOf("All", "Pasta", "Seafood", "Dessert", "Breakfast"),
    val recipes: List<Recipe> = emptyList(),
    val favorites: List<Recipe> = emptyList(),
    val isLoading: Boolean = false
)