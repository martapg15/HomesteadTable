package dam_a51564.homesteadtable.model

import com.google.firebase.firestore.PropertyName
import java.util.UUID

data class Ingredient(
    val name: String = "",
    val quantity: String = "",
    val unit: String = ""
) {
    // This property will return the quantity with the correct plural form. It also allows to have a
    // more dynamic UI, and not the default (s), e.g cup(s)
    val displayUnit: String
        get() {
            // Convert the quantity to a number to verify if the unit will be in singular or plural mode
            val qty = quantity.toDoubleOrNull() ?: 1.0
            return if (qty > 1.0) {
                when (unit) {
                    "cup" -> "cups"
                    "unit" -> "units"
                    else -> unit
                }
            } else {
                unit
            }
        }
}

data class Recipe(
    val id: String = UUID.randomUUID().toString(),
    val userId: String = "",
    val title: String = "",
    val category: String = "Main Course",
    val baseServings: Int = 2,
    val imageUrl: String = "",
    val ingredients: List<Ingredient> = emptyList(),
    val equipment: List<String> = emptyList(),
    val instructions: List<String> = emptyList(),
    @get:PropertyName("isFavourite") // Forces Firestore to map to "isFavourite" instead of "favourite"
    val isFavourite: Boolean = false
)