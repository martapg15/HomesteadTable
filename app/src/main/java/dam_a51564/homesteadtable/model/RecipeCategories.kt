package dam_a51564.homesteadtable.model

object RecipeCategories {
    // Used when creating or editing a recipe
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

    // Used on Home and Favourites screens to allow clearing the filter
    val filterList = listOf("All") + list
}