package dam_a51564.homesteadtable.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Label
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dam_a51564.homesteadtable.ui.theme.*

@Composable
fun HomeScreen(homeViewModel: HomeViewModel, onNavigateToFavourites: () -> Unit) {
    val homeUIState by homeViewModel.uiState.collectAsState()

    // State to keep track of which bottom nav item is selected
    var selectedBottomNavIndex by remember { mutableIntStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Cream,
                contentColor = Espresso
            ) {
                NavigationBarItem(
                    selected = true,
                    onClick = { /* Already here */ },
                    icon = { Icon(Icons.Default.Book, contentDescription = "Cookbook") },
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
                    selected = false,
                    onClick = onNavigateToFavourites,
                    icon = { Icon(Icons.Default.FavoriteBorder, contentDescription = "Favourites") },
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
                    onClick = { /* TODO */},
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
        // Main Content Area
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background) // Cream
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Header with Recipe Count
            Text(
                text = "My Cookbook,",
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(top = 20.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // List of data to loop through to avoid repeating UI code 3 times
                val stats = listOf(
                    Triple(homeUIState.recipes.size, "Recipes", Icons.Default.Book),
                    Triple(homeUIState.favorites.size, "Favourites", Icons.Default.FavoriteBorder),
                    Triple(homeUIState.categories.size, "Categories", Icons.AutoMirrored.Filled.Label)
                )

                stats.forEach { (count, label, icon) ->
                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, WarmTan.copy(alpha = 0.5f)),
                        color = White
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = WarmTan,
                                modifier = Modifier.size(18.dp)
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Column {
                                Text(
                                    text = count.toString(),
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = Black
                                )
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = WarmTan
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Search Bar and Add Button Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Search Bar takes up remaining space
                OutlinedTextField(
                    value = homeUIState.searchQuery,
                    onValueChange = { homeViewModel.onSearchQueryChange(it) },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Search your recipes", color = LightTan) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = LightTan) },
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = ParchmentBorder,
                        focusedBorderColor = Terracotta
                    ),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.width(16.dp))

                // "+" Button
                Button(
                    onClick = { /* TODO: Navigate to Create Recipe Screen */ },
                    modifier = Modifier.size(56.dp), // Matches the default height of OutlinedTextField
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Terracotta)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add New Recipe",
                        tint = Cream, // Keeps contrast high
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Category Pills (Scrollable Row)
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(homeUIState.categories) { category ->
                    val isSelected = homeUIState.selectedCategory == category
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isSelected) Terracotta else ParchmentBorder)
                            .clickable { homeViewModel.onCategorySelect(category) }
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

            // Empty State (If no recipes)
            if (homeUIState.recipes.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Book,
                        contentDescription = "Empty CookBook",
                        modifier = Modifier.size(64.dp),
                        tint = ParchmentBorder
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Your cookbook is empty.",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Espresso
                    )
                    Text(
                        text = "Tap the '+' button to add your first family recipe.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = WarmTan,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 8.dp, start = 32.dp, end = 32.dp)
                    )
                }
            }
        }
    }
}