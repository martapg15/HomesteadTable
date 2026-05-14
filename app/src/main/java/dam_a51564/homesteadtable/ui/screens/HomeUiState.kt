package dam_a51564.homesteadtable.ui.screens

data class HomeUiState(
    val searchQuery: String = "",
    val selectedCategory: String = "All",
    val favorites: List<String> = emptyList(),
    val categories: List<String> = listOf("All", "Pasta", "Seafood", "Dessert", "Breakfast"),
    //val recipes: List<String> = emptyList(),
    val recipes: List<String> = listOf("Creamy Tomato Pasta"),
    val isLoading: Boolean = false
)