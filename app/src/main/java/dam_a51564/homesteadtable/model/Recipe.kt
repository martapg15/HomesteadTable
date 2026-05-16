package dam_a51564.homesteadtable.model

import java.util.UUID

data class Ingredient(
    val name: String = "",
    val quantity: String = "",
    val unit: String = ""
)

data class Recipe(
    val id: String = UUID.randomUUID().toString(),
    val title: String = "",
    val category: String = "Pasta",
    val baseServings: Int = 2,
    val ingredients: List<Ingredient> = emptyList(),
    val equipment: List<String> = emptyList(),
    val instructions: List<String> = emptyList(),
    val isFavourite: Boolean = false
)