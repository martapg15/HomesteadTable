package dam_a51564.homesteadtable.ui.screens

// Enum to manage the toggle state
enum class DetailTab {
    INGREDIENTS, INSTRUCTIONS
}

data class RecipeDetailUiState(
    val title: String = "",
    val category: String = "",
    val portions: Int = 0,
    val equipment: List<String> = emptyList(),
    val ingredients: List<Ingredient> = emptyList(),
    val instructions: List<String> = emptyList(),
    val selectedTab: DetailTab = DetailTab.INGREDIENTS, // Defaults to Ingredients
    val isLoading: Boolean = true
)