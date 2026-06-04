package dam_a51564.homesteadtable.model

/**
 * Constants object containing structural configurations for supported recipe categorization lists.
 */
object RecipeCategories {
    /**
     * The master collection of standard categories available when creating or editing a recipe entry.
     */
    val list = listOf(
        "Main Course",
        "Appetizer",
        "Dessert",
        "Breakfast",
        "Snack",
        "Beverage",
        "Soup",
        "Salad"
    )

    /**
     * Filter list containing an "All" wildcard option, used on query selection hubs like Home and Favorites screens.
     */
    val filterList = listOf("All") + list
}