package dam_a51564.homesteadtable.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dam_a51564.homesteadtable.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailScreen(
    viewModel: RecipeDetailViewModel,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Espresso)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Cream)
            )
        },
        containerColor = Cream
    ) { paddingValues ->
        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Terracotta)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp)
            ) {
                // Title & Details
                Text(
                    text = uiState.title,
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = Espresso
                )
                Text(
                    text = "${uiState.category} • ${uiState.portions} Portions",
                    style = MaterialTheme.typography.bodyMedium,
                    color = WarmTan,
                    modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
                )

                // Equipment Section
                if (uiState.equipment.isNotEmpty()) {
                    Text(
                        text = "Equipment",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Espresso,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = uiState.equipment.joinToString(", "),
                        style = MaterialTheme.typography.bodyMedium,
                        color = WarmTan,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )
                }

                // Custom Tab Switcher (Ingredients vs Instructions)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(TerracottaLight)
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    val isIngredients = uiState.selectedTab == DetailTab.INGREDIENTS

                    Button(
                        onClick = { viewModel.selectTab(DetailTab.INGREDIENTS) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isIngredients) Terracotta else TerracottaLight,
                            contentColor = if (isIngredients) Cream else Espresso
                        ),
                        shape = RoundedCornerShape(10.dp),
                        elevation = ButtonDefaults.buttonElevation(0.dp),
                        contentPadding = PaddingValues(vertical = 12.dp)
                    ) {
                        Text("Ingredients", fontWeight = if (isIngredients) FontWeight.Bold else FontWeight.Medium)
                    }

                    Button(
                        onClick = { viewModel.selectTab(DetailTab.INSTRUCTIONS) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (!isIngredients) Terracotta else TerracottaLight,
                            contentColor = if (!isIngredients) Cream else Espresso
                        ),
                        shape = RoundedCornerShape(10.dp),
                        elevation = ButtonDefaults.buttonElevation(0.dp),
                        contentPadding = PaddingValues(vertical = 12.dp)
                    ) {
                        Text("Instructions", fontWeight = if (!isIngredients) FontWeight.Bold else FontWeight.Medium)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Scrollable List Content
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    if (uiState.selectedTab == DetailTab.INGREDIENTS) {
                        item {
                            Text(
                                text = "${uiState.ingredients.size} items",
                                style = MaterialTheme.typography.labelMedium,
                                color = WarmTan,
                                modifier = Modifier.padding(bottom = 12.dp)
                            )
                        }
                        items(uiState.ingredients) { ingredient ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = ingredient.name,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = Espresso
                                )
                                Text(
                                    text = "${ingredient.quantity} ${ingredient.unit}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Terracotta
                                )
                            }
                            HorizontalDivider(color = ParchmentBorder)
                        }
                    } else {
                        item {
                            Text(
                                text = "${uiState.instructions.size} steps",
                                style = MaterialTheme.typography.labelMedium,
                                color = WarmTan,
                                modifier = Modifier.padding(bottom = 12.dp)
                            )
                        }
                        itemsIndexed(uiState.instructions) { index, step ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp)
                            ) {
                                // Step Number Box
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(TerracottaLight),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "${index + 1}",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = Terracotta
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = step,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = Espresso
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}