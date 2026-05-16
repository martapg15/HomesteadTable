package dam_a51564.homesteadtable.ui.screens

import dam_a51564.homesteadtable.model.Recipe

// Enum to manage the toggle state
enum class DetailTab {
    INGREDIENTS, INSTRUCTIONS
}

data class RecipeDetailUiState(
    val recipe: Recipe? = null,
    val currentServings: Int = 1,
    val isLoading: Boolean = true,
    val selectedTab: DetailTab = DetailTab.INGREDIENTS,
    val errorMessage: String? = null
)