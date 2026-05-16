package dam_a51564.homesteadtable.ui.screens.home

import dam_a51564.homesteadtable.model.Recipe
import dam_a51564.homesteadtable.model.RecipeCategories

data class HomeUiState(
    val searchQuery: String = "",
    val selectedCategory: String = "All",
    val categories: List<String> = RecipeCategories.filterList,
    val recipes: List<Recipe> = emptyList(),
    val favorites: List<Recipe> = emptyList(),
    val isLoading: Boolean = false
)