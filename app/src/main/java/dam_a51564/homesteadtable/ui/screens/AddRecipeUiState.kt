package dam_a51564.homesteadtable.ui.screens

data class Ingredient(
    val name: String = "",
    val quantity: String = "",
    val unit: String = ""
)

data class AddRecipeUiState(
    val title: String = "",
    val selectedCategory: String = "Pasta",
    val portions: Int = 2,
    val ingredients: List<Ingredient> = listOf(Ingredient()),
    val steps: List<String> = listOf(""), // Changed to a list of steps
    val equipment: List<String> = listOf(""), // Ready for the UI
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
)