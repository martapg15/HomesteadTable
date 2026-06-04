package dam_a51564.homesteadtable.model

import com.google.firebase.firestore.PropertyName
import java.util.UUID

/**
 * Data class representing a distinct ingredient within a recipe specification.
 *
 * @property name The name/description of the ingredient (e.g., "Flour").
 * @property quantity The numerical or descriptive quantity string (e.g., "2", "2.5").
 * @property unit The standard unit of measurement associated with the ingredient (e.g., "cup", "g").
 */
data class Ingredient(
    val name: String = "",
    val quantity: String = "",
    val unit: String = ""
) {
    /**
     * Computes the display unit, factoring in pluralizations for specific unit names
     * depending on the quantity provided.
     */
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

/**
 * Data class representing a comprehensive Recipe document.
 * Maps seamlessly to and from Firestore items.
 *
 * @property id Unique UUID identifier generated on recipe creation.
 * @property userId The ownership identifier specifying which user created the recipe.
 * @property title The title or headline name of the recipe.
 * @property category The culinary grouping category (defaults to "Main Course").
 * @property baseServings The baseline portion count this configuration yields.
 * @property imageUrl The remote cloud hosting URL string for the recipe picture.
 * @property ingredients List of required components encapsulated as [Ingredient]s.
 * @property equipment List of required cooking utensils or machinery needed.
 * @property instructions Sequential recipe steps detailing preparation instructions.
 * @property isFavourite Boolean flag representing if this item was flagged as a favorite by the user.
 */
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
    @get:PropertyName("isFavourite") // Forces Firestore to map to "isFavourite" rather than default serialization
    val isFavourite: Boolean = false
)