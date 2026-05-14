package dam_a51564.homesteadtable.ui.screens

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class RecipeDetailViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(RecipeDetailUiState())
    val uiState: StateFlow<RecipeDetailUiState> = _uiState.asStateFlow()

    init {
        loadDummyRecipe()
    }

    // Function triggered by the UI buttons to switch between lists
    fun selectTab(tab: DetailTab) {
        _uiState.update { it.copy(selectedTab = tab) }
    }

    private fun loadDummyRecipe() {
        _uiState.update { state ->
            state.copy(
                title = "Creamy Tomato Pasta",
                category = "Pasta",
                portions = 2,
                equipment = listOf("Large Pot", "Skillet", "Wooden Spoon", "Colander"),
                ingredients = listOf(
                    Ingredient("Penne Pasta", "250", "g"),
                    Ingredient("Tomato Sauce", "1", "cup"),
                    Ingredient("Heavy Cream", "1/2", "cup"),
                    Ingredient("Garlic", "2", "cloves"),
                    Ingredient("Parmesan", "50", "g")
                ),
                instructions = listOf(
                    "Boil water in a large pot and cook pasta according to package instructions.",
                    "In a skillet, sauté minced garlic until fragrant.",
                    "Stir in tomato sauce and bring to a gentle simmer.",
                    "Reduce heat, pour in heavy cream, and mix well.",
                    "Drain pasta, add to the skillet, and toss to coat.",
                    "Serve hot, garnished with grated Parmesan cheese."
                ),
                isLoading = false
            )

        }
    }
}