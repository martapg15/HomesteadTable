package dam_a51564.homesteadtable.data

import dam_a51564.homesteadtable.model.Ingredient
import dam_a51564.homesteadtable.model.Recipe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object RecipeRepository {
    private val _recipes = MutableStateFlow<List<Recipe>>(emptyList())
    val recipes: StateFlow<List<Recipe>> = _recipes.asStateFlow()

    init {
        // Add one dummy recipe so your Home and Detail screens aren't empty immediately
        addRecipe(
            Recipe(
                id = "dummy_1",
                title = "Creamy Tomato Pasta",
                category = "Pasta",
                baseServings = 2,
                equipment = listOf("Large Pot", "Skillet"),
                ingredients = listOf(
                    Ingredient("Penne Pasta", "250", "g"),
                    Ingredient("Tomato Sauce", "1", "cup")
                ),
                instructions = listOf(
                    "Boil water and cook pasta.",
                    "Heat sauce in skillet and combine."
                )
            )
        )
    }

    fun addRecipe(recipe: Recipe) {
        _recipes.update { currentList -> currentList + recipe }
    }

    fun getRecipeById(id: String): Recipe? {
        return _recipes.value.find { it.id == id }
    }

    fun toggleFavourite(recipeId: String) {
        _recipes.update { currentList ->
            currentList.map { recipe ->
                if (recipe.id == recipeId) {
                    recipe.copy(isFavourite = !recipe.isFavourite)
                } else {
                    recipe
                }
            }
        }
    }
}