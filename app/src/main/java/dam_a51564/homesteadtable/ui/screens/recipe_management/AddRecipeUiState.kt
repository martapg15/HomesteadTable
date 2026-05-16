package dam_a51564.homesteadtable.ui.screens.recipe_management

import dam_a51564.homesteadtable.model.Ingredient
import dam_a51564.homesteadtable.model.Recipe
import dam_a51564.homesteadtable.model.RecipeCategories

data class AddRecipeUiState(
    val recipe: Recipe = Recipe(
        ingredients = listOf(Ingredient()),
        equipment = listOf(""),
        instructions = listOf("")
    ),
    val categories: List<String> = RecipeCategories.list,
    val isCategoryExpanded: Boolean = false,
    val isSaving: Boolean = false,
    val isSaved: Boolean = false,
    val errorMessage: String? = null
)