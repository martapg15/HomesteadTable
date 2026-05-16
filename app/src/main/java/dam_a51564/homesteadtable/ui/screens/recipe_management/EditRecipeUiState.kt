package dam_a51564.homesteadtable.ui.screens.recipe_management

import dam_a51564.homesteadtable.model.Recipe

data class EditRecipeUiState(
    val recipe: Recipe = Recipe(),
    val categories: List<String> = listOf("Pasta", "Seafood", "Dessert", "Breakfast", "Soups"),
    val isCategoryExpanded: Boolean = false,
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val isSaved: Boolean = false,
    val errorMessage: String? = null
)