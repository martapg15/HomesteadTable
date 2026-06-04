package dam_a51564.homesteadtable.ui.screens.detail

import dam_a51564.homesteadtable.model.Recipe

/**
 * Tab types available for selection within the Recipe Detail UI view hierarchy.
 */
enum class DetailTab {
    INGREDIENTS, INSTRUCTIONS
}

/**
 * UI state representing the current visual and data properties of the Recipe Detail screen.
 *
 * @property recipe The full [Recipe] instance currently being viewed, or null if loading/not found.
 * @property currentServings The dynamic serving multiplier adjusted by the user.
 * @property isLoading True when the recipe details are being fetched asynchronously.
 * @property selectedTab The active tab (Ingredients or Instructions) currently displayed.
 * @property errorMessage Contextual error message to display if loading or actions fail.
 * @property isDeleted True when the recipe has been successfully deleted, triggering navigation.
 */
data class RecipeDetailUiState(
    val recipe: Recipe? = null,
    val currentServings: Int = 1,
    val isLoading: Boolean = true,
    val selectedTab: DetailTab = DetailTab.INGREDIENTS,
    val errorMessage: String? = null,
    val isDeleted: Boolean = false
)