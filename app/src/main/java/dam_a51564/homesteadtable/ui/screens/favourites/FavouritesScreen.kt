package dam_a51564.homesteadtable.ui.screens.favourites

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dam_a51564.homesteadtable.ui.theme.*

@Composable
fun FavouritesScreen(
    favouritesViewModel: FavouritesViewModel,
    onNavigateToHome: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToRecipeDetail: (String) -> Unit
) {
    val favouritesUIState by favouritesViewModel.uiState.collectAsState()

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Cream,
                contentColor = Espresso
            ) {
                NavigationBarItem(
                    selected = false,
                    onClick = onNavigateToHome,
                    icon = { Icon(Icons.Default.Book, contentDescription = "CookBook") },
                    label = { Text("CookBook", style = MaterialTheme.typography.labelSmall) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Cream,
                        selectedTextColor = Terracotta,
                        indicatorColor = Terracotta,
                        unselectedIconColor = WarmTan,
                        unselectedTextColor = WarmTan
                    )
                )
                NavigationBarItem(
                    selected = true, // Favourites is currently selected
                    onClick = { /* Already here */ },
                    icon = { Icon(Icons.Filled.Favorite, contentDescription = "Favourites") },
                    label = { Text("Favourites", style = MaterialTheme.typography.labelSmall) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Cream,
                        selectedTextColor = Terracotta,
                        indicatorColor = Terracotta,
                        unselectedIconColor = WarmTan,
                        unselectedTextColor = WarmTan
                    )
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onNavigateToProfile,
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                    label = { Text("Profile", style = MaterialTheme.typography.labelSmall) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Cream,
                        selectedTextColor = Terracotta,
                        indicatorColor = Terracotta,
                        unselectedIconColor = WarmTan,
                        unselectedTextColor = WarmTan
                    )
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background) // Cream
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Header
            Text(
                text = "My Favourites,",
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(top = 20.dp)
            )
            Text(
                text = "${favouritesUIState.favouriteRecipes.size} saved recipes",
                style = MaterialTheme.typography.bodyLarge,
                color = WarmTan,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Search Bar (No Add Button needed here since users don't "create" a favourite from scratch)
            OutlinedTextField(
                value = favouritesUIState.searchQuery,
                onValueChange = { favouritesViewModel.onSearchQueryChange(it) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search your favourites", color = LightTan) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = LightTan) },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = ParchmentBorder,
                    focusedBorderColor = Terracotta
                ),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Category Pills
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(favouritesUIState.categories) { category ->
                    val isSelected = favouritesUIState.selectedCategory == category
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isSelected) Terracotta else ParchmentBorder)
                            .clickable { favouritesViewModel.onCategorySelect(category) }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = category,
                            color = if (isSelected) White else Espresso,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Empty State (Triggered when favouriteRecipes is empty)
            if (favouritesUIState.favouriteRecipes.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = "No Favourites",
                        modifier = Modifier.size(64.dp),
                        tint = ParchmentBorder
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "No Favourites Yet",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = Espresso
                    )

                    Text(
                        text = "Recipes you love will appear here.\nTap the ♡ on any recipe to save it.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = WarmTan,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 8.dp, start = 32.dp, end = 32.dp)
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(favouritesUIState.favouriteRecipes) { recipe ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onNavigateToRecipeDetail(recipe.id) },
                            colors = CardDefaults.cardColors(containerColor = TerracottaLight),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = recipe.title,
                                        style = MaterialTheme.typography.headlineSmall,
                                        color = Espresso
                                    )
                                    Text(
                                        text = "Tap to view full recipe",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = WarmTan,
                                        modifier = Modifier.padding(top = 4.dp)
                                    )
                                }
                                IconButton(
                                    onClick = { favouritesViewModel.toggleFavourite(recipe.id) }
                                ) {
                                    Icon(
                                        Icons.Filled.Favorite,
                                        contentDescription = "Remove Favourite",
                                        tint = Terracotta
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}