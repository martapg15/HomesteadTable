package dam_a51564.homesteadtable.model

import com.google.firebase.firestore.PropertyName
import java.util.UUID

data class Ingredient(
    val name: String = "",
    val quantity: String = "",
    val unit: String = ""
)

data class Recipe(
    val id: String = UUID.randomUUID().toString(),
    val userId: String = "",
    val title: String = "",
    val category: String = "Main Course",
    val baseServings: Int = 2,
    val ingredients: List<Ingredient> = emptyList(),
    val equipment: List<String> = emptyList(),
    val instructions: List<String> = emptyList(),
    @get:PropertyName("isFavourite") // Forces Firestore to map to "isFavourite" instead of "favourite"
    val isFavourite: Boolean = false
)